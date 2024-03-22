package com.cursos.api.springsecuritycourse.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-in-minutes}")
    private Long expirationInMinutes;

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);


    public String generateToken(UserDetails user, Map<String, Object> extraClaims ) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date( (expirationInMinutes*60*1000)+ issuedAt.getTime() );

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()

                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(extraClaims)

                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();

    }

    private SecretKey generateKey() {
        byte[] passwordDecoded = Decoders.BASE64.decode(secretKey);
        String passwordString = new String(passwordDecoded);
        LOGGER.info(passwordString);
        return Keys.hmacShaKeyFor(passwordDecoded);
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parser().verifyWith(generateKey()).build()
                .parseSignedClaims(jwt).getPayload();
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        //1. Obtener encabezado HTTP llamado Authorization
        String authorizationHeader = request.getHeader("Authorization"); //Bearer jwt
        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer")){
            return null;
        }

        //2. Obtener token JWT desde el encabezado
        return authorizationHeader.split(" ")[1];
    }

    public Date extractExpiration(String jwt) {
        return extractAllClaims(jwt).getExpiration();
    }
}

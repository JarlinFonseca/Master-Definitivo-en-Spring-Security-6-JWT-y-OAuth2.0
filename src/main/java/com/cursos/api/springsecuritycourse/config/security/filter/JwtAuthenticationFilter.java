package com.cursos.api.springsecuritycourse.config.security.filter;

import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.security.JwtToken;
import com.cursos.api.springsecuritycourse.persistence.entity.security.User;
import com.cursos.api.springsecuritycourse.persistence.repository.security.JwtTokenRepository;
import com.cursos.api.springsecuritycourse.service.UserService;
import com.cursos.api.springsecuritycourse.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final JwtTokenRepository jwtTokenRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        LOGGER.info("ENTRO EN EL FILTRO JWT AUTHENTICATION FILTER");

        //1. Obtener authorization header
        //2. Obtener token
        String jwt = jwtService.extractJwtFromRequest(request);
        if(!StringUtils.hasText(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        //2.1 Obtener token no expirado y valido desde base de datos.
        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);
        boolean isValid = validateToken(token);

        if(!isValid){
            filterChain.doFilter(request, response);
            return;
        }


        //3. Obtener el subject/username desde el encabezado esta acción a su vez valida el formato del token, firma y fecha de expiración
        String username = jwtService.extractUsername(jwt);


        //4. Setear objeto authentication dentro de security context holder
        User user= userService.findOneByUsername(username)
                .orElseThrow(()-> new ObjectNotFoundException("User not found. Username: "+username));

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username,null, user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5. Ejecutar el registro de filtros
        filterChain.doFilter(request, response);

    }

    private boolean validateToken(Optional<JwtToken> optionalJwtToken) {
        if(!optionalJwtToken.isPresent()) {
            LOGGER.info("Token no existe o no fue generado en nuestro sistema");
            return false;
        }

        JwtToken token = optionalJwtToken.get();
        Date now = new Date(System.currentTimeMillis());

        boolean isValid = token.isValid() && token.getExpiration().after(now);
        if(!isValid){
            LOGGER.error("Token inválido");
            updateTokenStatus(token);
        }
        return isValid;

    }

    private void updateTokenStatus(JwtToken token) {
        token.setValid(false);
        jwtTokenRepository.save(token);
    }
}

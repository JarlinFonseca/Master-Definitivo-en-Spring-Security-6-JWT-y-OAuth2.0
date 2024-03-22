package com.cursos.api.springsecuritycourse.service.auth;

import com.cursos.api.springsecuritycourse.dto.RegisteredUser;
import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationRequest;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationResponse;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.security.JwtToken;
import com.cursos.api.springsecuritycourse.persistence.entity.security.User;
import com.cursos.api.springsecuritycourse.persistence.repository.security.JwtTokenRepository;
import com.cursos.api.springsecuritycourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository jwtTokenRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        User user = userService.registerOneCustomer(newUser);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveUserToken(user, jwt);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public AuthenticationResponse login(AuthenticationRequest authRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()
        );

        authenticationManager.authenticate(authentication);

        UserDetails user = userService.findOneByUsername(authRequest.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));
        saveUserToken((User) user, jwt);

        AuthenticationResponse authRsp = new AuthenticationResponse();
        authRsp.setJwt(jwt);

        return authRsp;
    }

    private void saveUserToken(User user, String jwt) {
        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);

        jwtTokenRepository.save(token);
    }

    public boolean validateToken(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public User findLoggedInUser() {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String username = (String) auth.getPrincipal();

        return userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

    }

    public Boolean validateAuthLoggedByUsername(String username) {

        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String usernameAuth = (String) auth.getPrincipal();

        return (usernameAuth.equals(username));

    }

    public void logout(HttpServletRequest request) {
        String jwt = jwtService.extractJwtFromRequest(request);
        if (!StringUtils.hasText(jwt)) return;

        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);

        if(token.isPresent() && token.get().isValid()){
            token.get().setValid(false);
            jwtTokenRepository.save(token.get());
        }
    }

}

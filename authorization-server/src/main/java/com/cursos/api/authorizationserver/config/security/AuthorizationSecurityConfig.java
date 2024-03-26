package com.cursos.api.authorizationserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
@EnableWebSecurity
public class AuthorizationSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults());

        http.exceptionHandling(exceptionConfig -> {
            exceptionConfig.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        });

        http.oauth2ResourceServer(oauth2ResourceServerConfig -> {
            oauth2ResourceServerConfig.jwt(withDefaults());
        });

        return http.build();
    }
    
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers("/login").permitAll();
                    authConfig.anyRequest().authenticated();
                });

        http.formLogin(withDefaults());

        return http.build();
    }


}

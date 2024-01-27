package com.cursos.api.springsecuritycourse.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityBeanInjector {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
    }
}

package com.cursos.api.springsecuritycourse.config.security;

import com.cursos.api.springsecuritycourse.config.security.filter.JwtAuthenticationFilter;
import com.cursos.api.springsecuritycourse.persistence.util.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig {


    private final AuthenticationProvider daoAuthProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authReqConfig ->
                    authReqConfig.anyRequest().access(authorizationManager)
                )
                .exceptionHandling(exceptionConfig -> {
                    exceptionConfig.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionConfig.accessDeniedHandler(accessDeniedHandler);
                })
                .build();

    }

    @Profile({"local", "dev"})
    @Bean
    CorsConfigurationSource defaultCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList( "http://127.0.0.1:5500", "https://www.google.com"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Profile("docker")
    @Bean
    CorsConfigurationSource dockerCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://client"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static void buildRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
            /*
              Autorización de enpoints de products
             */
        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());


        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasRole(RoleEnum.ADMINISTRATOR.name());


//        authReqConfig.requestMatchers(HttpMethod.PUT,"/products/{productId}")
        authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/products/[0-9]*"))
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
                .hasRole(RoleEnum.ADMINISTRATOR.name());


            /*
              Autorización de enpoints de categories
             */

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());


        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasRole(RoleEnum.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
                .hasRole(RoleEnum.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name(), RoleEnum.CUSTOMER.name());


            /*
              Autorización de enpoints públicos
             */

        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/customers").denyAll();

        authReqConfig.anyRequest().authenticated();
    }

}

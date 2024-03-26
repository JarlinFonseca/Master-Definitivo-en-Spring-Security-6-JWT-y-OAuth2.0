package com.cursos.api.authorizationserver.mapper;

import com.cursos.api.authorizationserver.persistence.entity.security.ClientApp;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Date;


public class ClientAppMapper {

    private ClientAppMapper() {
    }

    public static RegisteredClient toRegisteredClient(ClientApp clientApp) {
        return RegisteredClient.withId(clientApp.getClientId())
                .clientId(clientApp.getClientId())
                .clientSecret(clientApp.getClientSecret())
                .clientIdIssuedAt(new Date(System.currentTimeMillis()).toInstant())
                .clientAuthenticationMethods(clientAuthMethods -> clientApp.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethod::new)
                        .forEach(clientAuthMethods::add))
                .authorizationGrantTypes(authGrantTypes -> clientApp.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantType::new)
                        .forEach(authGrantTypes::add))
                .redirectUris(redirectUris -> redirectUris.addAll(clientApp.getRedirectUris()))
                .scopes(scopes -> scopes.addAll(clientApp.getScopes()))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(clientApp.getDurationInMinutes()))
                        .refreshTokenTimeToLive(Duration.ofMinutes(clientApp.getDurationInMinutes() * 4L))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(clientApp.isRequireProofKey())
                        .build())
                .build();
    }
}

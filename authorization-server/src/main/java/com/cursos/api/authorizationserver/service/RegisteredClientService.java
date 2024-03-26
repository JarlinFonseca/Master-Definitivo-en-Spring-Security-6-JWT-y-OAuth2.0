package com.cursos.api.authorizationserver.service;

import com.cursos.api.authorizationserver.exception.ObjectNotFoundException;
import com.cursos.api.authorizationserver.mapper.ClientAppMapper;
import com.cursos.api.authorizationserver.persistence.entity.security.ClientApp;
import com.cursos.api.authorizationserver.persistence.repository.security.ClientAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisteredClientService implements RegisteredClientRepository {

    private final ClientAppRepository clientAppRepository;
    @Override
    public void save(RegisteredClient registeredClient) {
        // SAVE document why this method is empty
    }

    @Override
    public RegisteredClient findById(String id) {
        ClientApp clientApp =clientAppRepository.findByClientId(id)
                .orElseThrow(()-> new ObjectNotFoundException("Client not found"));
        return ClientAppMapper.toRegisteredClient(clientApp);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return this.findById(clientId);
    }
}

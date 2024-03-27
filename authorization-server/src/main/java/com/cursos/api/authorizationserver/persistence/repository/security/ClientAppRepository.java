package com.cursos.api.authorizationserver.persistence.repository.security;

import com.cursos.api.authorizationserver.persistence.entity.security.ClientApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientAppRepository extends JpaRepository<ClientApp, Long> {

    Optional<ClientApp> findByClientId(String clientId);
}

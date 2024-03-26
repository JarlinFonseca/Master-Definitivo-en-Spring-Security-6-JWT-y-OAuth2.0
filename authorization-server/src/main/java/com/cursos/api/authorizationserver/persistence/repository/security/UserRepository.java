package com.cursos.api.authorizationserver.persistence.repository.security;

import com.cursos.api.authorizationserver.persistence.entity.security.User;;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

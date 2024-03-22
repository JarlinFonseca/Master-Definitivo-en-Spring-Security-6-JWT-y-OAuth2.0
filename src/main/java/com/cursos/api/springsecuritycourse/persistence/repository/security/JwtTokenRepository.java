package com.cursos.api.springsecuritycourse.persistence.repository.security;

import com.cursos.api.springsecuritycourse.persistence.entity.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
}

package com.cursos.api.springsecuritycourse.service;

import com.cursos.api.springsecuritycourse.persistence.entity.security.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
}

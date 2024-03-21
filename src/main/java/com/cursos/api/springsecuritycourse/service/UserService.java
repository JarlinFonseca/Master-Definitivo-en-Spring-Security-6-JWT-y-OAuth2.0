package com.cursos.api.springsecuritycourse.service;

import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.persistence.entity.security.User;

import java.util.Optional;

public interface UserService {
    User registerOneCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String username);
}

package com.cursos.api.springsecuritycourse.service;

import com.cursos.api.springsecuritycourse.dto.SavePermission;
import com.cursos.api.springsecuritycourse.dto.ShowPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PermissionService {
    Page<ShowPermission> findAll(Pageable pageable);

    Optional<ShowPermission> findOneById(Long permissionId);

    ShowPermission createOne(SavePermission savePermission);

    ShowPermission deleteOne(Long permissionId);
}

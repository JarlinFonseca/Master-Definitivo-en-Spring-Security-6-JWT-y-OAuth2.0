package com.cursos.api.springsecuritycourse.service.impl;

import com.cursos.api.springsecuritycourse.dto.SavePermission;
import com.cursos.api.springsecuritycourse.dto.ShowPermission;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.security.GrantedPermission;
import com.cursos.api.springsecuritycourse.persistence.entity.security.Operation;
import com.cursos.api.springsecuritycourse.persistence.entity.security.Role;
import com.cursos.api.springsecuritycourse.persistence.repository.security.GrantedPermissionRepository;
import com.cursos.api.springsecuritycourse.persistence.repository.security.OperationRepository;
import com.cursos.api.springsecuritycourse.persistence.repository.security.RoleRepository;
import com.cursos.api.springsecuritycourse.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final GrantedPermissionRepository grantedPermissionRepository;
    private final OperationRepository operationRepository;
    private final RoleRepository roleRepository;

    @Override
    public Page<ShowPermission> findAll(Pageable pageable) {
        Page<GrantedPermission> permissions = grantedPermissionRepository.findAll(pageable);

        List<ShowPermission> showPermissions = permissions.stream().map(PermissionServiceImpl::mapEntityToShowDto)
                .toList();

        return new PageImpl<>(showPermissions, pageable, permissions.getTotalElements());
    }

    @Override
    public Optional<ShowPermission> findOneById(Long permissionId) {
        return grantedPermissionRepository.findById(permissionId).map(PermissionServiceImpl::mapEntityToShowDto);
    }

    @Override
    public ShowPermission createOne(SavePermission savePermission) {
        Operation operation = operationRepository.findByName(savePermission.getOperation())
                .orElseThrow(() -> new ObjectNotFoundException("Operation not found"));

        Role role = roleRepository.findByName(savePermission.getRole()).
                orElseThrow(() -> new ObjectNotFoundException("Role not found"));

        GrantedPermission grantedPermission = new GrantedPermission();
        grantedPermission.setOperation(operation);
        grantedPermission.setRole(role);

        grantedPermissionRepository.save(grantedPermission);

        return mapEntityToShowDto(grantedPermission);

    }

    @Override
    public ShowPermission deleteOne(Long permissionId) {
        GrantedPermission grantedPermission = grantedPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new ObjectNotFoundException("Permission not found"));
        grantedPermissionRepository.delete(grantedPermission);
        return mapEntityToShowDto(grantedPermission);
    }

    private static ShowPermission mapEntityToShowDto(GrantedPermission grantedPermission) {
        return ShowPermission.builder()
                .id(grantedPermission.getId())
                .operation(grantedPermission.getOperation().getName())
                .httpMethod(grantedPermission.getOperation().getHttpMethod())
                .module(grantedPermission.getOperation().getModule().getName())
                .role(grantedPermission.getRole().getName()).build();
    }
}

package com.cursos.api.springsecuritycourse.persistence.repository.security;

import com.cursos.api.springsecuritycourse.persistence.entity.security.GrantedPermission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GrantedPermissionRepository extends JpaRepository<GrantedPermission, Long> {

}

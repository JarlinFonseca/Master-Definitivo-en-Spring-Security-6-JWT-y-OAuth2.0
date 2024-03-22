package com.cursos.api.springsecuritycourse.persistence.repository.security;

import com.cursos.api.springsecuritycourse.persistence.entity.security.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long>{

    @Query("SELECT o FROM Operation o WHERE o.permitAll = true")
    List<Operation> findByPublicAccess();

    Optional<Operation> findByName(String operation);
}

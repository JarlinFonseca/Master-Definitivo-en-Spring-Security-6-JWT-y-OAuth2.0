package com.cursos.api.springsecuritycourse.persistence.repository;

import com.cursos.api.springsecuritycourse.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

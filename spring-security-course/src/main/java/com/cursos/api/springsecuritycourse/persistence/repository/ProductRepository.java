package com.cursos.api.springsecuritycourse.persistence.repository;

import com.cursos.api.springsecuritycourse.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

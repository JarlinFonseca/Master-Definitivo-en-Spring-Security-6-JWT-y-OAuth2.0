package com.cursos.api.springsecuritycourse.service;

import com.cursos.api.springsecuritycourse.dto.SaveProduct;
import com.cursos.api.springsecuritycourse.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

public interface ProductService {
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findOneById(Long productId);

    Product createOne(SaveProduct saveProduct);

    Product updateOneById(Long productId, SaveProduct saveProduct);

    Product disableOneById(Long productId);
}

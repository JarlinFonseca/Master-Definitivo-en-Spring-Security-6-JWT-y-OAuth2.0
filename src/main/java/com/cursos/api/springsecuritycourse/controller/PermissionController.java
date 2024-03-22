package com.cursos.api.springsecuritycourse.controller;

import com.cursos.api.springsecuritycourse.dto.SavePermission;
import com.cursos.api.springsecuritycourse.dto.ShowPermission;
import com.cursos.api.springsecuritycourse.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<Page<ShowPermission>> findAll(Pageable pageable) {
        Page<ShowPermission> permissions = permissionService.findAll(pageable);

        if (permissions.hasContent()) {
            return ResponseEntity.ok(permissions);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<ShowPermission> findOneById(@PathVariable Long permissionId) {
        Optional<ShowPermission> permission = permissionService.findOneById(permissionId);

        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<ShowPermission> createOne(@RequestBody @Valid SavePermission savePermission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createOne(savePermission));
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<ShowPermission> deleteOne(@PathVariable Long permissionId) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.deleteOne(permissionId));
    }
}

package com.cursos.api.springsecuritycourse.controller;

import com.cursos.api.springsecuritycourse.dto.RegisteredUser;
import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.persistence.entity.security.User;
import com.cursos.api.springsecuritycourse.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final AuthenticationService authenticationService;

    @PreAuthorize("permitAll")
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser){
        RegisteredUser registeredUser =  authenticationService.registerOneCustomer(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

    }

    @PreAuthorize("denyAll")
    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(List.of());
    }
}

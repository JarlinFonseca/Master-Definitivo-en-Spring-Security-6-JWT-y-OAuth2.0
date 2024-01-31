package com.cursos.api.springsecuritycourse.controller;

import com.cursos.api.springsecuritycourse.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/{username}")
    public ResponseEntity<String> validateUsernameLoggedByUsername(@PathVariable(name = "username")String username) throws AccessDeniedException {
        String rta = "";
        if(authenticationService.validateAuthLoggedByUsername(username)){
            rta = "The username passed has access, since it is the one that is currently authenticated.";
        }else{
            rta= "The username passed has no access, it is NOT the authenticated user.";
            throw new AccessDeniedException("The username passed has no access, it is NOT the authenticated user.");
        }
        return ResponseEntity.ok(rta);
    }
}

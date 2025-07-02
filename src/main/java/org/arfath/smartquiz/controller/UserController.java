package org.arfath.smartquiz.controller;

import org.arfath.smartquiz.model.UserEntity;
import org.arfath.smartquiz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }




}

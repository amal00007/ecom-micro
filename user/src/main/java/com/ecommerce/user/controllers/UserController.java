package com.ecommerce.user.controllers;


import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/users/{id}")
    public List<UserResponse> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        boolean createdUser= userService.createUser(userRequest);
        if(createdUser)
            return ResponseEntity.ok("User created");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
        boolean updatedUser = userService.updateUser(id, userRequest);
        if (updatedUser) {
            return ResponseEntity.ok("User updated");
        }
        return ResponseEntity.notFound().build();
    }
}

package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.exception.UnauthorizedException;
import com.berkinozturk.event.request.UpdateUserRequest;
import com.berkinozturk.event.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.createUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getFullName(), user.getRole()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("id") String id) {
        Optional<UserEntity> user = userService.findUserById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateProfile(@PathVariable("id") String id, @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateProfile(id, request.getFullName(), request.getEmail()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

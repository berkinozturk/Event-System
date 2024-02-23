package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.request.UpdateUserRequest;
import com.berkinozturk.event.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // UserEntity is something you want to write own internal business logic around. You do not want to send that to the user.
    // Write a CreateUserResponse and then, write a mapper that maps UserEntity to CreateUserResponse and then, use it in here. Also write test for that mapping functionality.
    // Moreover, this controller here is specific to the Restful API and that is not part of our business logic.
    // Today it is synchronous and might be asynchronous tomorrow and, you poll from Kafka. You'd ideally want things separated.
    // Your business logic components such as entities ideally should not come up to this level. Try to keep them at service level.
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
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

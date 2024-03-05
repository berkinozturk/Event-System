package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.request.UpdateUserRequest;
import com.berkinozturk.event.response.CreateUserResponse;
import com.berkinozturk.event.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("1", "username", "password", "email@example.com", "Full Name", RoleType.USER);
    }

    @Test
    void testCreateUser() {
        UserEntity userEntity = new UserEntity("1", "username", "password", "email@example.com", "Full Name", RoleType.USER);
        when(userService.createUser(userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail(), userEntity.getFullName(), userEntity.getRole()))
                .thenReturn(userEntity);

        ResponseEntity<CreateUserResponse> responseEntity = userController.createUser(userEntity);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userEntity.getId(), responseEntity.getBody().getId());
        assertEquals(userEntity.getUsername(), responseEntity.getBody().getUsername());
        assertEquals(userEntity.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(userEntity.getFullName(), responseEntity.getBody().getFullName());
        assertEquals(userEntity.getRole(), responseEntity.getBody().getRole());
    }

    @Test
    void getUserById_ValidId_ReturnsUserEntity() {
        // Given
        when(userService.findUserById("1")).thenReturn(Optional.of(userEntity));

        // When
        ResponseEntity<UserEntity> response = userController.getUserById("1");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
    }

    @Test
    void getUserById_InvalidId_ThrowsEntityNotFoundException() {
        // Given
        when(userService.findUserById("invalidId")).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> userController.getUserById("invalidId"));
    }

    @Test
    void updateProfile_ValidId_ReturnsUpdatedUserEntity() {
        // Given
        UpdateUserRequest request = new UpdateUserRequest("New Full Name", "newemail@example.com");
        when(userService.updateProfile("1", "New Full Name", "newemail@example.com")).thenReturn(userEntity);

        // When
        ResponseEntity<UserEntity> response = userController.updateProfile("1", request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
    }

    @Test
    void deleteUser_ValidId_ReturnsNoContent() {
        // When
        ResponseEntity<Void> response = userController.deleteUser("1");

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser("1");
    }
}

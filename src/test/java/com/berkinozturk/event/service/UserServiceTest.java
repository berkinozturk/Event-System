package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.mapper.UserMapper;
import com.berkinozturk.event.repository.UserRepository;
import com.berkinozturk.event.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static com.berkinozturk.event.entity.RoleType.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("1", "username", "password", "email@example.com", "Full Name", USER);
    }

    @Test
    void createUser_ValidUser_ReturnsUserEntity() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email@example.com");
        registerRequest.setFullName("Full Name");
        registerRequest.setRole(USER);

        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setId("1"); // assuming user id is set after creation
        expectedUserEntity.setUsername("username");
        expectedUserEntity.setEmail("email@example.com");
        expectedUserEntity.setFullName("Full Name");
        expectedUserEntity.setRole(USER);

        when(userMapper.toUserEntity(any(RegisterRequest.class))).thenReturn(expectedUserEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);

        // When
        UserEntity createdUser = userService.createUser(registerRequest);

        // Then
        assertEquals(expectedUserEntity, createdUser);
    }


    @Test
    void findUserById_ValidId_ReturnsUserEntity() {
        // Given
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

        // When
        Optional<UserEntity> foundUser = userService.findUserById("1");

        // Then
        assertEquals(userEntity, foundUser.orElse(null));
    }

    @Test
    void findUserById_InvalidId_ReturnsEmptyOptional() {
        // Given
        when(userRepository.findById("invalidId")).thenReturn(Optional.empty());

        // When
        Optional<UserEntity> foundUser = userService.findUserById("invalidId");

        // Then
        assertEquals(Optional.empty(), foundUser);
    }

    @Test
    void updateProfile_ValidId_ReturnsUpdatedUserEntity() {
        // Given
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any())).thenReturn(userEntity);

        // When
        UserEntity updatedUser = userService.updateProfile("1", "New Full Name", "newemail@example.com");

        // Then
        assertEquals(userEntity, updatedUser);
        assertEquals("New Full Name", updatedUser.getFullName());
        assertEquals("newemail@example.com", updatedUser.getEmail());
    }

    @Test
    void updateProfile_InvalidId_ThrowsEntityNotFoundException() {
        // Given
        when(userRepository.findById("invalidId")).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> userService.updateProfile("invalidId", "New Full Name", "newemail@example.com"));
    }

    @Test
    void deleteUser_ValidId_DeletesUserEntity() {
        // Given
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

        // When
        userService.deleteUser("1");

        // Then
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteUser_InvalidId_ThrowsEntityNotFoundException() {
        // Given
        when(userRepository.findById("invalidId")).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser("invalidId"));
    }
}

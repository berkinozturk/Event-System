package com.berkinozturk.event.integration;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.mapper.RegisterRequestToUserMapper;
import com.berkinozturk.event.repository.UserRepository;
import com.berkinozturk.event.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


// Integration test is where you put together all the components you use and test a certain flow(s).
// It's good that we're actually using the database and the caching solution we have but this does not guarantee
// That the system will work as expected.
// The application is Restful API therefore, you need to boot your Spring application and then, send requests like:
// One scenario: First authenticate and then, create event, update event, get events and then, delete events.
// Another one is: Try to make requests to the protected endpoints without authenticating.
// Make sure that your authentication solution covers those.
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(UserService.class)
@EnableCaching
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        UserEntity user = new UserEntity("1", "testuser", "password", "test@example.com", "Test User", RoleType.USER);
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    }

    @AfterEach
    public void clearCache() {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void testFindUserById() {
        Optional<UserEntity> user = userService.findUserById("1");
        assertTrue(user.isPresent());
        assertEquals("testuser", user.get().getUsername());
    }


    @Test
    public void testFindUserByUsername() {
        Optional<UserEntity> user = userService.findUserByUsername("testuser");
        assertTrue(user.isPresent());
        assertEquals("test@example.com", user.get().getEmail());
    }

    @Test
    public void testUpdateProfile() {
        String userId = "1";
        String newFullName = "Updated Name";
        String newEmail = "updated@example.com";

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity updatedUser = userService.updateProfile(userId, newFullName, newEmail);

        assertNotNull(updatedUser);

        assertEquals(newFullName, updatedUser.getFullName());
        assertEquals(newEmail, updatedUser.getEmail());
        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
    }


    @Test
    public void testDeleteUser() {
        String userId = "1";
        userService.deleteUser(userId);
        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testFindUserByUsername_NotFound() {
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> foundUser = userService.findUserByUsername(username);

        assertTrue(foundUser.isEmpty());
    }


    @Test
    public void testFindUserById_NotFound() {
        String userId = "999";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserEntity> foundUser = userService.findUserById(userId);

        assertTrue(foundUser.isEmpty());
    }

}

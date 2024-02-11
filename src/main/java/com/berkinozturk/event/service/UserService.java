package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userEntityRepository;

    @CachePut(value = "users", key = "#user.id")
    public UserEntity createUser(String username, String password, String email, String fullName, RoleType role) {
        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .email(email)
                .fullName(fullName)
                .role(role)
                .build();

        return userEntityRepository.save(user);
    }

    @Cacheable(value = "users", key = "#userId")
    public Optional<UserEntity> findUserById(String userId) {
        return userEntityRepository.findById(userId);
    }

    @Cacheable(value = "users", key = "#username")
    public Optional<UserEntity> findUserByUsername(String username) {
        return userEntityRepository.findByUsername(username);
    }

    @CachePut(value = "users", key = "#userId")
    public UserEntity updateProfile(String userId, String fullName, String email) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setFullName(fullName);
        user.setEmail(email);
        return userEntityRepository.save(user);
    }
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(String userId) {
        userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        userEntityRepository.deleteById(userId);
    }





}

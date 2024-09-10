package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.mapper.UserMapper;
import com.berkinozturk.event.repository.UserRepository;
import com.berkinozturk.event.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userEntityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserEntity createUser(RegisterRequest request) {
        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userEntityRepository.save(user);
    }

    public Optional<UserEntity> findUserById(String userId) {
        return userEntityRepository.findById(userId);
    }


    public Optional<UserEntity> findUserByUsername(String username) {
        return userEntityRepository.findByUsername(username);
    }

    // Where do you set the user to the cache?
    // ANSWER ==>
    // Actually I set the cache only in here and deleteUser methods, because other methods are not returning the user object.
    // Caching can be deleted from here and delete method.
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

package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.mapper.RegisterRequestToUserMapper;
import com.berkinozturk.event.repository.UserRepository;
import com.berkinozturk.event.request.RegisterRequest;
import com.berkinozturk.event.response.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userEntityRepository;
    private final RegisterRequestToUserMapper registerRequestToUserMapper;
    private final PasswordEncoder passwordEncoder;



    public CreateUserResponse createUser(RegisterRequest request) {
        try {
            UserEntity user = registerRequestToUserMapper.toUserEntity(request);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntityRepository.save(user);

            return new CreateUserResponse(user.getUsername(), user.getPassword(), user.getEmail(), user.getFullName(), RoleType.USER);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
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

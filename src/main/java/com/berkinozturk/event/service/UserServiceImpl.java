package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.exception.UnauthorizedException;
import com.berkinozturk.event.repository.EventRepository;
import com.berkinozturk.event.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userEntityRepository;
    private final EventRepository eventRepository;

    @Override
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

    @Override
    public Optional<UserEntity> findUserById(String userId) {
        return userEntityRepository.findById(userId);
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return userEntityRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String userId, RoleType role) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setRole(role);
        userEntityRepository.save(user);
    }

    public UserEntity updateProfile(String userId, String fullName, String email) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!isCurrentUser(userId)) {
            throw new UnauthorizedException("You are not authorized to update this user's profile");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        return userEntityRepository.save(user);
    }
    public void deleteUser(String userId) {
        userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        userEntityRepository.deleteById(userId);
    }

    private boolean isCurrentUser(String userId) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return currentUserId.equals(userId);
    }




}

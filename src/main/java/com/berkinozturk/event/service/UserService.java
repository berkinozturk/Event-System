package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    UserEntity createUser(String username, String password, String email, String fullName, RoleType role);

    Optional<UserEntity> findUserById(String userId);

    Optional<UserEntity> findUserByUsername(String username);

    void addRoleToUser(String userId, RoleType role);


}

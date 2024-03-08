package com.berkinozturk.event.mapper;

import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.request.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toUserEntity(RegisterRequest request) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole())
                .build();
    }
}

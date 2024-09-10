package com.berkinozturk.event.service;

import com.berkinozturk.event.config.JwtService;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.UserNotFoundException;
import com.berkinozturk.event.repository.UserRepository;
import com.berkinozturk.event.request.AuthenticationRequest;
import com.berkinozturk.event.request.RegisterRequest;
import com.berkinozturk.event.response.AuthenticationResponse;
import com.berkinozturk.event.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationResponse register(RegisterRequest request) {
        UserEntity user = userMapper.toUserEntity(request);
        userRepository.save(user);
        try {
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException("Token generation failed: " + ex.getMessage());
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid credentials: Bad credentials", ex);
        }
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}

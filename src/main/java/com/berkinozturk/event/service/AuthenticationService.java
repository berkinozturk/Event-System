package com.berkinozturk.event.service;

import com.berkinozturk.event.config.JwtService;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.repository.UserRepository;
import com.berkinozturk.event.request.AuthenticationRequest;
import com.berkinozturk.event.request.RegisterRequest;
import com.berkinozturk.event.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Mapping request object which is an input from an external world to an internal is a good practice.
        // Can be improved with a Mapper class with a mapper function. Why?
        // Because you can write tests for that mapping function and.
        // You guarantee that these string values are always passed to the correct field.
        // For instance, if I set username to the password field what's stopping me now?
        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole())
                .build();

        // What if creating a user is successful but generating a token fails, what happens?
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // Throws what?
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

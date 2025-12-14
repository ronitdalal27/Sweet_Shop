package com.example.sweetshop.service;

import com.example.sweetshop.dto.AuthRequest;
import com.example.sweetshop.dto.AuthResponse;
import com.example.sweetshop.entity.User;
import com.example.sweetshop.exception.BadRequestException;
import com.example.sweetshop.repository.UserRepository;
import com.example.sweetshop.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(AuthRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        String role = request.getRole();

        if (role == null || role.isBlank()) {
            role = "ROLE_USER";
        } else {
            role = role.toUpperCase();

            if (role.equals("ADMIN")) {
                role = "ROLE_ADMIN";
            } else if (role.equals("USER")) {
                role = "ROLE_USER";
            } else {
                throw new BadRequestException("Invalid role: " + role);
            }
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        return "User registered successfully with role " + role;
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new BadRequestException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());

        return new AuthResponse("Login successful", token);
    }
}

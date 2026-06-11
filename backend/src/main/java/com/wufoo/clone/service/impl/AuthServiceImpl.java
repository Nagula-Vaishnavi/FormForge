package com.wufoo.clone.service.impl;

import com.wufoo.clone.config.JwtUtil;
import com.wufoo.clone.dto.request.LoginRequest;
import com.wufoo.clone.dto.response.AuthResponse;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.exception.UnauthorizedException;
import com.wufoo.clone.repository.UserRepository;
import com.wufoo.clone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Get user details
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());
        System.out.println("========== JWT TOKEN ==========");
        System.out.println(token);
        System.out.println("===============================");

        // Build response
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}

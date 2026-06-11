package com.wufoo.clone.controller;

import com.wufoo.clone.config.CustomUserDetails;
import com.wufoo.clone.config.JwtUtil;
import com.wufoo.clone.dto.request.LoginRequest;
import com.wufoo.clone.dto.request.RegisterRequest;
import com.wufoo.clone.dto.response.AuthResponse;
import com.wufoo.clone.dto.response.UserResponse;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.service.AuthService;
import com.wufoo.clone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.registerUser(registerRequest);

        // Generate JWT token for the newly registered user
        String token = jwtUtil.generateToken(userResponse.getEmail());
        System.out.println("========== REGISTRATION JWT TOKEN ==========");
        System.out.println(token);
        System.out.println("==========================================");

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(userResponse.getId())
                .email(userResponse.getEmail())
                .fullName(userResponse.getFullName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userDetails.getUser();
        UserResponse userResponse = userService.findUserByEmail(user.getEmail());
        return ResponseEntity.ok(userResponse);
    }
}

package com.wufoo.clone.service.impl;

import com.wufoo.clone.dto.request.RegisterRequest;
import com.wufoo.clone.dto.request.UserUpdateRequest;
import com.wufoo.clone.dto.response.UserResponse;
import com.wufoo.clone.entity.Role;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.exception.BadRequestException;
import com.wufoo.clone.exception.ResourceNotFoundException;
import com.wufoo.clone.repository.UserRepository;
import com.wufoo.clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already registered: " + registerRequest.getEmail());
        }

        // Create new user
        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
 
        // Save user
        @SuppressWarnings("null")
        User savedUser = userRepository.save(user);

        // Map to response
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponse updateUserProfile(String currentEmail, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + currentEmail));

        // Check if new email is different and already exists
        if (!user.getEmail().equals(userUpdateRequest.getEmail()) && 
            userRepository.existsByEmail(userUpdateRequest.getEmail())) {
            throw new BadRequestException("Email already registered: " + userUpdateRequest.getEmail());
        }

        // Update user details
        user.setFullName(userUpdateRequest.getFullName());
        user.setEmail(userUpdateRequest.getEmail());

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

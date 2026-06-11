package com.wufoo.clone.service;

import com.wufoo.clone.dto.request.RegisterRequest;
import com.wufoo.clone.dto.request.UserUpdateRequest;
import com.wufoo.clone.dto.response.UserResponse;

public interface UserService {

    /**
     * Register a new user
     * @param registerRequest the registration details
     * @return the created user response
     * @throws RuntimeException if email already exists
     */
    UserResponse registerUser(RegisterRequest registerRequest);

    /**
     * Find a user by email address
     * @param email the email address to search for
     * @return the user response if found
     * @throws RuntimeException if user not found
     */
    UserResponse findUserByEmail(String email);

    /**
     * Check if an email already exists in the system
     * @param email the email address to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Update user profile
     * @param currentEmail the current email of the user
     * @param userUpdateRequest the updated user details
     * @return the updated user response
     */
    UserResponse updateUserProfile(String currentEmail, UserUpdateRequest userUpdateRequest);
}

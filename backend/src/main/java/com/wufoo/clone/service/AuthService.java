package com.wufoo.clone.service;

import com.wufoo.clone.dto.request.LoginRequest;
import com.wufoo.clone.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Authenticate user and generate JWT token
     * @param loginRequest the login credentials
     * @return AuthResponse with JWT token and user details
     * @throws RuntimeException if authentication fails
     */
    AuthResponse login(LoginRequest loginRequest);
}

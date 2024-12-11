package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.AuthRequest;
import com.enigma.jobConnector.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    AuthResponse refreshToken(String token);
    void logout(String accessToken);

}

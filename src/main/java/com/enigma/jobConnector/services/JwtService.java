package com.enigma.jobConnector.services;


import com.enigma.jobConnector.entity.User;

public interface JwtService {
    String generateToken(User user);
    String getUserIdFromToken(String token);
    void blacklistAccessToken(String bearerToken);
    boolean isTokenBlacklisted(String token);
}

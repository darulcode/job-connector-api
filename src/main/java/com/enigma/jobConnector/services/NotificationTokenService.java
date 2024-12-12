package com.enigma.jobConnector.services;

import com.enigma.jobConnector.entity.User;

public interface NotificationTokenService {
    void create(String token, User user);
}

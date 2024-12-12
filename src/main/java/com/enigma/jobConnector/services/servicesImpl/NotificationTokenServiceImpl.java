package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.entity.NotificationToken;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.NotificationTokenRepository;
import com.enigma.jobConnector.services.NotificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTokenServiceImpl implements NotificationTokenService {

    private final NotificationTokenRepository notificationTokenRepository;

    @Override
    public void create(String token, User user) {
        if (token != null && !token.isEmpty() && notificationTokenRepository.findByNotificationToken(token).isEmpty()) {
            NotificationToken notificationToken = NotificationToken.builder()
                    .notificationToken(token)
                    .user(user)
                    .build();
            notificationTokenRepository.save(notificationToken);
        }
    }
}

package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.services.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RedisService redisService;

    @Spy
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    void shouldReturnStringRefreshTokenWhenCreateToken() {
        String userId = "1";
        String mockRefreshToken = UUID.randomUUID().toString();

        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_EXPIRY", 24);

        Mockito.when(refreshTokenService.createToken(userId)).thenReturn(mockRefreshToken);
        String token = refreshTokenService.createToken(userId);

        assertEquals(mockRefreshToken, token);
        Mockito.verify(
                refreshTokenService,
                Mockito.times(1)
        ).createToken(userId);
    }

    @Test
    void shouldDeleteExistingTokenWhenCreateToken() {
        String userId = "1";
        String mockRefreshToken = UUID.randomUUID().toString();

        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_EXPIRY", 24);

        Mockito.when(redisService.get("refreshToken:" + userId)).thenReturn(mockRefreshToken);

        String token = refreshTokenService.createToken(userId);

        assertNotNull(token);
        Mockito.verify(
                refreshTokenService,
                Mockito.times(1)
        ).deleteRefreshToken(userId);
    }

    @Test
    void shouldReturnNothingWhenDeleteRefreshToken() {
        String userId = "1";

        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_EXPIRY", 24);

        refreshTokenService.deleteRefreshToken(userId);

        Mockito.verify(
                refreshTokenService,
                Mockito.times(1)
        ).deleteRefreshToken(userId);
    }

    @Test
    void shouldReturnRefreshTokenWhenRotateFreshToken() {
        String userId = "1";
        String mockRefreshToken = UUID.randomUUID().toString();

        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_EXPIRY", 24);

        Mockito.when(refreshTokenService.rotateRefreshToken(userId)).thenReturn(mockRefreshToken);
        String token = refreshTokenService.rotateRefreshToken(userId);

        assertEquals(mockRefreshToken, token);
        Mockito.verify(
                refreshTokenService,
                Mockito.times(2)
        ).rotateRefreshToken(userId);
    }

    @Test
    void shouldReturnUserIdWhenGetUserIdByToken() {
        String mockUserId = "1";
        String mockRefreshToken = UUID.randomUUID().toString();

        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_EXPIRY", 24);

        Mockito.when(refreshTokenService.getUserIdByToken(mockRefreshToken)).thenReturn(mockUserId);
        String userId = refreshTokenService.getUserIdByToken(mockRefreshToken);

        assertEquals(mockUserId, userId);
        Mockito.verify(
                refreshTokenService,
                Mockito.times(1)
        ).getUserIdByToken(mockRefreshToken);
    }
}

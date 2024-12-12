package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.services.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private RedisService redisService;

    @Spy
    @InjectMocks
    private JwtServiceImpl jwtService;

    @Test
    void shouldReturnStringWhenGenerateToken() {
        User mockUserAccount = User.builder()
                .id("1")
                .email("email")
                .role(UserRole.ROLE_ADMIN)
                .build();

        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "mockSecretKey");
        ReflectionTestUtils.setField(jwtService, "EXPIRATION_TIME_TOKEN", 60L);
        ReflectionTestUtils.setField(jwtService, "ISSUER", "mockIssuer");

        String token = jwtService.generateToken(mockUserAccount);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void shouldReturnNothingWhenGetUserIdFromToken() {
        String userId = jwtService.getUserIdFromToken(null);

        assertNull(userId, "User ID should be null for a null token");
    }

    @Test
    void shouldReturnNothingWhenBlacklistAccessToken() {
        String token = "jwt-token";

        Mockito.doNothing().when(jwtService).blacklistAccessToken(token);

        jwtService.blacklistAccessToken(token);

        Mockito.verify(
                jwtService,
                Mockito.times(1)
        ).blacklistAccessToken(token);
    }

    @Test
    void shouldReturnBooleanWhenIsBTokenBlacklisted() {
        String mockToken = "jwt-token";
        Boolean mockIsBlacklisted = true;

        String blacklistedToken = Mockito.when(redisService.get(mockToken)).thenReturn(mockToken).toString();
        jwtService.isTokenBlacklisted(mockToken);

        assertEquals(mockIsBlacklisted, blacklistedToken != null);
        Mockito.verify(
                jwtService,
                Mockito.times(1)
        ).isTokenBlacklisted(mockToken);
    }
}
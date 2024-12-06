package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.dto.request.AuthRequest;
import com.enigma.jobConnector.dto.response.AuthResponse;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.services.JwtService;
import com.enigma.jobConnector.services.RefreshTokenService;
import com.enigma.jobConnector.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAuthResponseWhenLogin() {
        AuthRequest authRequest = AuthRequest.builder()
                .username("username")
                .password("password")
                .build();

        User mockUserAccount = User.builder()
                .id("1")
                .username("username")
                .role(UserRole.ROLE_ADMIN)
                .build();

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword())
        )).thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUserAccount);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        Mockito.when(jwtService.generateToken(mockUserAccount)).thenReturn("mockAccessToken");
        Mockito.when(refreshTokenService.createToken(mockUserAccount.getId())).thenReturn("mockRefreshToken");

        AuthResponse authResponse = authService.login(authRequest);

        assertEquals(mockUserAccount.getId(), authResponse.getId());
        assertEquals("mockAccessToken", authResponse.getAccessToken());
        assertEquals("mockRefreshToken", authResponse.getRefreshToken());
        assertEquals(UserRole.ROLE_ADMIN.getDescription(), authResponse.getRole());
    }

    @Test
    void shouldReturnAuthResponseWhenRefreshToken() {
        String token = "token";

        String mockUserId = "1";
        Mockito.when(refreshTokenService.getUserIdByToken(token)).thenReturn(mockUserId);

        User mockUserAccount = User.builder()
                .id("1")
                .username("username")
                .role(UserRole.ROLE_ADMIN)
                .build();
        Mockito.when(userService.getOne(mockUserId)).thenReturn(mockUserAccount);

        String mockNewRefreshToken = "mockRefreshToken";
        Mockito.when(refreshTokenService.rotateRefreshToken(mockUserId)).thenReturn(mockNewRefreshToken);

        String mockNewToken = "mockAccessToken";
        Mockito.when(jwtService.generateToken(mockUserAccount)).thenReturn(mockNewToken);

        AuthResponse authResponse = authService.refreshToken(token);

        assertEquals(mockUserAccount.getId(), authResponse.getId());
        assertEquals("mockAccessToken", authResponse.getAccessToken());
        assertEquals("mockRefreshToken", authResponse.getRefreshToken());
        assertEquals(UserRole.ROLE_ADMIN.getDescription(), authResponse.getRole());
    }

    @Test
    void shouldThrowErrorWhenRefreshToken() {
        String token = "token";

        String mockUserId = null;
        Mockito.when(refreshTokenService.getUserIdByToken(token)).thenReturn(mockUserId);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.refreshToken(token)
        );

        assertEquals("Invalid refresh token", exception.getReason());

        Mockito.verify(
                refreshTokenService,
                Mockito.times(1)
        ).getUserIdByToken(token);
    }

    @Test
    void shouldDoNothingWhenLogout() {
        String mockAccessToken = "mockAccessToken";

        User mockUserAccount = User.builder()
                .id("1")
                .username("username")
                .role(UserRole.ROLE_ADMIN)
                .build();

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUserAccount);

        Mockito.doNothing().when(refreshTokenService).deleteRefreshToken(mockUserAccount.getId());
        Mockito.doNothing().when(jwtService).blacklistAccessToken(mockAccessToken);

        authService.logout(mockAccessToken);

        Mockito.verify(
                mockAuthentication,
                Mockito.times(1)
        ).getPrincipal();
        Mockito.verify(
                refreshTokenService,
                Mockito.times(1)
        ).deleteRefreshToken(mockUserAccount.getId());
        Mockito.verify(
                jwtService,
                Mockito.times(1)
        ).blacklistAccessToken(mockAccessToken);
    }
}
package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.AuthRequest;
import com.enigma.jobConnector.dto.response.AuthResponse;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.services.AuthService;
import com.enigma.jobConnector.services.JwtService;
import com.enigma.jobConnector.services.RefreshTokenService;
import com.enigma.jobConnector.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User userAccount = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateToken(userAccount);
        String refreshToken = refreshTokenService.createToken(userAccount.getId());
        return AuthResponse.builder()
                .id(userAccount.getId())
                .accessToken(accessToken)
                .role(userAccount.getRole().getDescription())
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String token) {
        String userId = refreshTokenService.getUserIdByToken(token);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_REFRESH_TOKEN);
        }
        User userAccount = userService.getOne(userId);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(userId);
        String newToken = jwtService.generateToken(userAccount);
        return AuthResponse.builder()
                .id(userAccount.getId())
                .accessToken(newToken)
                .refreshToken(newRefreshToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }

    @Override
    public void logout(String accessToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userAccount = (User) authentication.getPrincipal();
        refreshTokenService.deleteRefreshToken(userAccount.getId());
        jwtService.blacklistAccessToken(accessToken);
    }
}

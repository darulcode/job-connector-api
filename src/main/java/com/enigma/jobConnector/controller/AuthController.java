package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.AuthRequest;
import com.enigma.jobConnector.dto.request.RefreshTokenRequest;
import com.enigma.jobConnector.dto.response.AuthResponse;
import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.services.AuthService;
import com.enigma.jobConnector.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RequiredArgsConstructor
@RestController
@RequestMapping(Constant.AUTH_API)
@Tag(name = "Authentication", description = "APIs for user authentication, login, logout and refresh token")
public class AuthController {

    @Value("${enigmajobConnector.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;

    private final AuthService authService;

    @Operation(summary = "Login",
            description = "API for auth user can access to portal",
            responses = {
                    @ApiResponse(responseCode = "200", description = Constant.SUCCESS_LOGIN_MESSAGE, content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = Constant.INVALID_CREDENTIAL, content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        AuthResponse login = authService.login(authRequest);
        setCookie(response, login.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_LOGIN_MESSAGE, login);
    }

    @Operation(summary = "refresh token with cookie",
            description = "API for auth user can get new accessToken, using cookie on browser storage",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PostMapping(path = "/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.OK, authResponse);
    }


    @Operation(summary = "refresh token with body",
            description = "API for auth user can get new accessToken, refreshToken must be send on body",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = Constant.INVALID_CREDENTIAL, content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PostMapping(path = "/refresh-token-mobile")
    public ResponseEntity<?> refreshTokenMobile(@RequestBody RefreshTokenRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.OK, authResponse);
    }


    @Operation(summary = "Logout",
            description = "API for auth user stop auth the access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = Constant.INVALID_CREDENTIAL, content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(bearerToken);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.OK, null);
    }


    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(Constant.REFRESH_TOKEN_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.REFRESH_TOKEN_REQUIRED_MESSAGE));
        return cookie.getValue();
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(Constant.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * REFRESH_TOKEN_EXPIRY);
        response.addCookie(cookie);
    }
}

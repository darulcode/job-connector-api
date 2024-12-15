package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.AuthRequest;
import com.enigma.jobConnector.dto.response.AuthResponse;
import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.services.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void shouldReturn200WhenLogin() throws Exception {
        AuthResponse mockAuthResponse = AuthResponse.builder()
                .id("1")
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .role("Admin")
                .build();

        Mockito.when(authService.login(Mockito.any(AuthRequest.class))).thenReturn(mockAuthResponse);

        String requestBody = """
            {
                "email": "username",
                "password": "password"
            }
            """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.AUTH_API + "/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<AuthResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(response);
        assertEquals(mockAuthResponse.getId(), response.getData().getId());
        assertEquals(mockAuthResponse.getAccessToken(), response.getData().getAccessToken());
        assertEquals(mockAuthResponse.getRefreshToken(), response.getData().getRefreshToken());
        assertEquals(mockAuthResponse.getRole(), response.getData().getRole());
    }

    @Test
    void shouldReturn200WhenRefreshToken() throws Exception {
        String mockAccessToken = "mockAccessToken";
        AuthResponse mockAuthResponse = AuthResponse.builder()
                .id("1")
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .role("Admin")
                .build();

        Mockito.when(authService.refreshToken(mockAccessToken)).thenReturn(mockAuthResponse);

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.AUTH_API + "/refresh-token")
                        .cookie(new Cookie(Constant.REFRESH_TOKEN_COOKIE_NAME, mockAccessToken))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<AuthResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(response);
        assertEquals(mockAuthResponse.getId(), response.getData().getId());
        assertEquals(mockAuthResponse.getAccessToken(), response.getData().getAccessToken());
        assertEquals(mockAuthResponse.getRefreshToken(), response.getData().getRefreshToken());
        assertEquals(mockAuthResponse.getRole(), response.getData().getRole());
    }

    @Test
    void shouldReturn200WhenRefreshTokenMobile() throws Exception {
        String mockRefreshToken = "mockRefreshToken";
        AuthResponse mockAuthResponse = AuthResponse.builder()
                .id("1")
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .role("Admin")
                .build();

        Mockito.when(authService.refreshToken(mockRefreshToken)).thenReturn(mockAuthResponse);

        String requestBody = """
            {
                "refreshToken": "mockRefreshToken"
            }
            """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.AUTH_API + "/refresh-token-mobile")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<AuthResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(response);
        assertEquals(mockAuthResponse.getId(), response.getData().getId());
        assertEquals(mockAuthResponse.getAccessToken(), response.getData().getAccessToken());
        assertEquals(mockAuthResponse.getRefreshToken(), response.getData().getRefreshToken());
        assertEquals(mockAuthResponse.getRole(), response.getData().getRole());
    }

    @Test
    @WithMockUser
    void shouldReturn200WhenLogout() throws Exception {
        String mockBearerToken = "Bearer mockBearerToken";
        Mockito.doNothing().when(authService).logout(mockBearerToken);

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.AUTH_API + "/logout")
                .header(HttpHeaders.AUTHORIZATION, mockBearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<?> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(Constant.SUCCESS_LOGOUT, response.getMessage());

        Mockito.verify(authService, Mockito.times(1)).logout(mockBearerToken);
    }


}
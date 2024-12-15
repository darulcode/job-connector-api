package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.ChangePasswordRequest;
import com.enigma.jobConnector.dto.request.ForgotPasswordRequest;
import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn201WhenCreateUser() throws Exception {
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("UserCategory")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .id("1")
                .name("name")
                .email("email@email.com")
                .phoneNumber("081111112222")
                .role("Trainee")
                .category(userCategoryResponse)
                .build();

        Mockito.when(userService.create(Mockito.any(UserRequest.class))).thenReturn(userResponse);

        String requestBody = """
                {
                    "name": "name",
                    "phoneNumber": "081111112222",
                    "password": "password",
                    "email":  "email@email.com",
                    "categoryId": "1",
                    "role": "Trainee"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.USER_API)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(userResponse.getId(), response.getData().getId());
        assertEquals(userResponse.getName(), response.getData().getName());
        assertEquals(userResponse.getEmail(), response.getData().getEmail());
        assertEquals(userResponse.getRole(), response.getData().getRole());
        assertEquals(userCategoryResponse.getId(), response.getData().getCategory().getId());
        assertEquals(userCategoryResponse.getName(), response.getData().getCategory().getName());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenUpdateUser() throws Exception {
        String id = "1";
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("UserCategory")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .id("1")
                .name("name")
                .email("email@email.com")
                .phoneNumber("081111112222")
                .role("Trainee")
                .category(userCategoryResponse)
                .build();

        Mockito.when(userService.update(Mockito.eq(id), Mockito.any(UserRequest.class))).thenReturn(userResponse);

        String requestBody = """
                {
                    "name": "name",
                    "phoneNumber": "081111112222",
                    "password": "password",
                    "email":  "email@email.com",
                    "categoryId": "1",
                    "role": "Trainee"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.put(Constant.USER_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(userResponse.getId(), response.getData().getId());
        assertEquals(userResponse.getName(), response.getData().getName());
        assertEquals(userResponse.getEmail(), response.getData().getEmail());
        assertEquals(userResponse.getRole(), response.getData().getRole());
        assertEquals(userCategoryResponse.getId(), response.getData().getCategory().getId());
        assertEquals(userCategoryResponse.getName(), response.getData().getCategory().getName());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn2000WhenDeleteUser() throws Exception {
        String id = "1";

        Mockito.doNothing().when(userService).delete(id);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(Constant.USER_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userService, Mockito.times(1)).delete(id);
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void shouldReturn200WhenMe() throws Exception {
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("UserCategory")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .id("1")
                .name("name")
                .email("email@email.com")
                .phoneNumber("081111112222")
                .role("Trainee")
                .category(userCategoryResponse)
                .build();

        Mockito.when(userService.getSelfUserDetails()).thenReturn(userResponse);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.get(Constant.USER_API + "/me")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(userResponse.getId(), response.getData().getId());
        assertEquals(userResponse.getName(), response.getData().getName());
        assertEquals(userResponse.getEmail(), response.getData().getEmail());
        assertEquals(userResponse.getRole(), response.getData().getRole());
        assertEquals(userCategoryResponse.getId(), response.getData().getCategory().getId());
        assertEquals(userCategoryResponse.getName(), response.getData().getCategory().getName());
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void shouldReturn200WhenChangePassword() throws Exception {
        Mockito.doNothing().when(userService).changePassword(Mockito.any(ChangePasswordRequest.class));

        String requestBody = """
                {
                    "oldPassword": "1",
                    "newPassword": "2"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.put(Constant.USER_API + "/change-password")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userService, Mockito.times(1)).changePassword(Mockito.any(ChangePasswordRequest.class));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenGetUser() throws Exception {
        String id = "1";
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("UserCategory")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .id("1")
                .name("name")
                .email("email@email.com")
                .phoneNumber("081111112222")
                .role("Trainee")
                .category(userCategoryResponse)
                .build();

        Mockito.when(userService.getUserDetails(Mockito.eq(id))).thenReturn(userResponse);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.get(Constant.USER_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(userResponse.getId(), response.getData().getId());
        assertEquals(userResponse.getName(), response.getData().getName());
        assertEquals(userResponse.getEmail(), response.getData().getEmail());
        assertEquals(userResponse.getRole(), response.getData().getRole());
        assertEquals(userCategoryResponse.getId(), response.getData().getCategory().getId());
        assertEquals(userCategoryResponse.getName(), response.getData().getCategory().getName());
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void shouldReturn200WhenForgotPassword() throws Exception {
        String email = "email@email.com";

        Mockito.doNothing().when(userService).sendForgotPassword(email);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.USER_API + "/forgot/" + email)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userService, Mockito.times(1)).sendForgotPassword(email);
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void shouldReturn200WhenVerifyCode() throws Exception {
        String code = "1234";

        Mockito.doNothing().when(userService).checkValidCode(code);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.get(Constant.USER_API + "/verify/" + code)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userService, Mockito.times(1)).checkValidCode(code);
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void shouldReturn200WhenChangePasswordForgot() throws Exception {
        Mockito.doNothing().when(userService).changePassword(Mockito.any(ForgotPasswordRequest.class));

        String requestBody = """
                {
                    "newPassword": "1",
                    "code": "1234"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.USER_API + "/forgot")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userService, Mockito.times(1)).changePassword(Mockito.any(ForgotPasswordRequest.class));
    }
}
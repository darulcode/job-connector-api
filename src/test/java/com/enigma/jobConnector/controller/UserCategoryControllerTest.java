package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.services.UserCategoryService;
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
class UserCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserCategoryService userCategoryService;

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn201WhenCreateUserCategory() throws Exception {
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("category name")
                .build();

        Mockito.when(userCategoryService.createUserCategory(Mockito.any(UserCategoryRequest.class))).thenReturn(userCategoryResponse);

        String requestBody = """
                {
                    "name": "category name"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.USER_CATEGORY_API)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(userCategoryResponse.getId(), response.getData().getId());
        assertEquals(userCategoryResponse.getName(), response.getData().getName());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenUpdateUserCategory() throws Exception {
        String id = "1";
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("category name")
                .build();

        Mockito.when(userCategoryService.updateUserCategory(Mockito.eq(id), Mockito.any(UserCategoryRequest.class))).thenReturn(userCategoryResponse);

        String requestBody = """
                {
                    "name": "category name"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.put(Constant.USER_CATEGORY_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(userCategoryResponse.getId(), response.getData().getId());
        assertEquals(userCategoryResponse.getName(), response.getData().getName());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenGetUserCategory() throws Exception {
        String id = "1";
        UserCategoryResponse userCategoryResponse = UserCategoryResponse.builder()
                .id("1")
                .name("category name")
                .build();

        Mockito.when(userCategoryService.getById(Mockito.eq(id))).thenReturn(userCategoryResponse);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.get(Constant.USER_CATEGORY_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userCategoryService, Mockito.times(1)).getById(id);
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenDeleteUserCategory() throws Exception {
        String id = "1";

        Mockito.doNothing().when(userCategoryService).delete(Mockito.eq(id));

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(Constant.USER_CATEGORY_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<UserResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(userCategoryService, Mockito.times(1)).delete(id);
    }
}
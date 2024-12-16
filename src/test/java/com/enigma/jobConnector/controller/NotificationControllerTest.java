package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.dto.response.NotificationResponse;
import com.enigma.jobConnector.services.NotificationService;
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
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @Test
    @WithMockUser(roles = "TRAINEE")
    void shouldReturn200WhenReadNotification() throws Exception {
        String notificationId = "1";
        NotificationResponse notificationResponse = NotificationResponse.builder()
                .id("1")
                .body("body")
                .screen("screen")
                .isRead(true)
                .testId("test-id-1")
                .userId("user-id-1")
                .build();

        Mockito.when(notificationService.readNotification(Mockito.eq(notificationId))).thenReturn(notificationResponse);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/notification/read/" + notificationId)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<NotificationResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(notificationResponse.getId(), response.getData().getId());
        assertEquals(notificationResponse.getBody(), response.getData().getBody());
        assertEquals(notificationResponse.getScreen(), response.getData().getScreen());
        assertEquals(notificationResponse.getIsRead(), response.getData().getIsRead());
        assertEquals(notificationResponse.getTestId(), response.getData().getTestId());
        assertEquals(notificationResponse.getUserId(), response.getData().getUserId());
    }
}
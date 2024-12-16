package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.UpdateStatusSubmissionRequest;
import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.services.TestDetailService;
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
class TestDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TestDetailService testDetailService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200WhenUpdateTest() throws Exception {
        String id = "1";
        TestDetailResponse testDetailResponse = TestDetailResponse.builder()
                .id("id")
                .userId("user-id")
                .name("name")
                .category("category")
                .status("Pending")
                .submissionText("submission-text")
                .fileSubmission("file-submission")
                .build();

        Mockito.when(testDetailService.updateStatusSubmission(Mockito.eq(id), Mockito.any(UpdateStatusSubmissionRequest.class))).thenReturn(testDetailResponse);

        String requestBody = """
                {
                    "status": "Pending"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.put(Constant.TEST_DETAIL_API + "/update/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<TestDetailResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(testDetailResponse.getId(), response.getData().getId());
        assertEquals(testDetailResponse.getUserId(), response.getData().getUserId());
        assertEquals(testDetailResponse.getName(), response.getData().getName());
        assertEquals(testDetailResponse.getCategory(), response.getData().getCategory());
        assertEquals(testDetailResponse.getStatus(), response.getData().getStatus());
        assertEquals(testDetailResponse.getSubmissionText(), response.getData().getSubmissionText());
        assertEquals(testDetailResponse.getFileSubmission(), response.getData().getFileSubmission());
    }
}
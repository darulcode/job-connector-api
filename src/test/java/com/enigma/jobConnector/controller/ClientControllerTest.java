package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.ClientRequest;
import com.enigma.jobConnector.dto.response.ClientResponse;
import com.enigma.jobConnector.dto.response.CommonResponse;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.services.ClientService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenGetClient() throws Exception {
        String id = "1";
        ClientResponse clientResponse = ClientResponse.builder()
                .id("1")
                .name("name")
                .address("address")
                .build();

        Mockito.when(clientService.getClientDetail(Mockito.eq(id))).thenReturn(clientResponse);

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.get(Constant.CLIENT_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<ClientResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(clientResponse.getId(), response.getData().getId());
        assertEquals(clientResponse.getName(), response.getData().getName());
        assertEquals(clientResponse.getAddress(), response.getData().getAddress());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn201WhenCreateClient() throws Exception {
        ClientResponse clientResponse = ClientResponse.builder()
                .id("1")
                .name("name")
                .address("address")
                .build();

        Mockito.when(clientService.createClient(Mockito.any(ClientRequest.class))).thenReturn(clientResponse);

        String requestBody = """
                {
                    "name": "name",
                    "address": "address"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.post(Constant.CLIENT_API)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<ClientResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(clientResponse.getId(), response.getData().getId());
        assertEquals(clientResponse.getName(), response.getData().getName());
        assertEquals(clientResponse.getAddress(), response.getData().getAddress());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenUpdateClient() throws Exception {
        String id = "1";
        ClientResponse clientResponse = ClientResponse.builder()
                .id("1")
                .name("name")
                .address("address")
                .build();

        Mockito.when(clientService.updateClient(Mockito.eq(id), Mockito.any(ClientRequest.class))).thenReturn(clientResponse);

        String requestBody = """
                {
                    "name": "name",
                    "address": "address"
                }
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.put(Constant.CLIENT_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<ClientResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(clientResponse.getId(), response.getData().getId());
        assertEquals(clientResponse.getName(), response.getData().getName());
        assertEquals(clientResponse.getAddress(), response.getData().getAddress());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void shouldReturn200WhenDeleteClient() throws Exception {
        String id = "1";

        Mockito.doNothing().when(clientService).deleteClient(Mockito.eq(id));

        String requestBody = """
                """;

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(Constant.CLIENT_API + "/" + id)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CommonResponse<ClientResponse> response = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(response);
        Mockito.verify(clientService, Mockito.times(1)).deleteClient(id);
    }
}
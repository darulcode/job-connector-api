package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.ClientRequest;
import com.enigma.jobConnector.dto.request.ClientSearchRequest;
import com.enigma.jobConnector.dto.response.ClientResponse;
import com.enigma.jobConnector.entity.Client;
import org.springframework.data.domain.Page;

public interface ClientService {
    ClientResponse createClient(ClientRequest clientRequest);
    ClientResponse updateClient(String id,ClientRequest clientRequest);
    void deleteClient(String id);
    Client getOne(String id);
    ClientResponse getClientDetail(String id);
    Page<ClientResponse> getAllClients(ClientSearchRequest request);
}

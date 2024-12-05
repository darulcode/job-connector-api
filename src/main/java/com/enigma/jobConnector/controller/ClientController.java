package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.ClientRequest;
import com.enigma.jobConnector.dto.request.ClientSearchRequest;
import com.enigma.jobConnector.dto.response.ClientResponse;
import com.enigma.jobConnector.services.ClientService;
import com.enigma.jobConnector.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.CLIENT_API)
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "name", required = false) String query) {

        ClientSearchRequest clientSearchRequest = ClientSearchRequest.builder()
                .query(query)
                .sortBy(sortBy)
                .size(size)
                .page(page)
                .build();
        Page<ClientResponse> response = clientService.getAllClients(clientSearchRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_FETCH_ALL_CLIENT, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        ClientResponse response = clientService.getClientDetail(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCH_CLIENT, response);
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientRequest request) {
        ClientResponse response = clientService.createClient(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_CLIENT, response);
    }

    @PutMapping
    public ResponseEntity<?> updateClient(@RequestBody ClientRequest request) {
        ClientResponse response = clientService.updateClient(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_CLIENT, response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_CLIENT, null);
    }

}

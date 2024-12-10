package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.ClientRequest;
import com.enigma.jobConnector.dto.request.ClientSearchRequest;
import com.enigma.jobConnector.dto.response.ClientResponse;
import com.enigma.jobConnector.services.ClientService;
import com.enigma.jobConnector.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.CLIENT_API)
@RequiredArgsConstructor
@Tag(name = "Client", description = "APIs for user user, get all, create, get by id, update and delete client")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "find all client")
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

    @Operation(summary = "get client by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        ClientResponse response = clientService.getClientDetail(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCH_CLIENT, response);
    }

    @Operation(summary = "create client")
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientRequest request) {
        ClientResponse response = clientService.createClient(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_CLIENT, response);
    }

    @Operation(summary = "update client")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable String id,@RequestBody ClientRequest request) {
        ClientResponse response = clientService.updateClient(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_CLIENT, response);
    }

    @Operation(summary = "delete client")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_CLIENT, null);
    }

}

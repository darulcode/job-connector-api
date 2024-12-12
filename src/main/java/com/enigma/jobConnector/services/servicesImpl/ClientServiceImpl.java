package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.dto.request.ClientRequest;
import com.enigma.jobConnector.dto.request.ClientSearchRequest;
import com.enigma.jobConnector.dto.response.ClientResponse;
import com.enigma.jobConnector.entity.Client;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.ClientRepository;
import com.enigma.jobConnector.services.ClientService;
import com.enigma.jobConnector.specification.ClientSpecification;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import com.enigma.jobConnector.utils.ShortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ClientResponse createClient(ClientRequest clientRequest) {
        validateCurrentUserRole();
        clientRepository.findByName(clientRequest.getName()).ifPresent(client -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.CLIENT_ALREADY_EXIST);
        });
        Client client = Client.builder()
                .name(clientRequest.getName())
                .address(clientRequest.getAddress())
                .build();
        clientRepository.saveAndFlush(client);
        return getClientResponse(client);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ClientResponse updateClient(String id,ClientRequest clientRequest) {
        validateCurrentUserRole();
        Client client = getOne(id);
        client.setName(clientRequest.getName());
        client.setAddress(clientRequest.getAddress());
        clientRepository.saveAndFlush(client);
        return getClientResponse(client);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteClient(String id) {
        validateCurrentUserRole();
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.CLIENT_NOT_FOUND);
        }
        clientRepository.delete(client.get());
    }

    @Transactional(readOnly = true)
    @Override
    public Client getOne(String id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.CLIENT_NOT_FOUND, null));
    }

    @Transactional(readOnly = true)
    @Override
    public ClientResponse getClientDetail(String id) {
        validateCurrentUser();
        return getClientResponse(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ClientResponse> getAllClients(ClientSearchRequest request) {
        validateCurrentUser();
        Sort sortBy = ShortUtil.parseSort(request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Specification<Client> clientSpecification = ClientSpecification.getSpecification(request);
        Page<Client> client = clientRepository.findAll(clientSpecification, pageable);
        return client.map(this::getClientResponse);

    }

    private ClientResponse getClientResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }

    private void validateCurrentUserRole() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
        if (!UserRole.ROLE_SUPER_ADMIN.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
    }

    private void validateCurrentUser() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
    }
}

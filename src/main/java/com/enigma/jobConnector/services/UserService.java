package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User getOne(String id);
    UserResponse create(UserRequest userRequest);
    User findByUsername(String username);
    UserResponse update(String id, UserRequest userRequest);
    void delete(String id);
    Page<UserResponse> findAllUser(UserSearchRequest userSearchRequest);
    void batchCreate(List<User> users);
    List<User> findAll();
    UserResponse getUserDetails(String id);
    UserResponse getSelfUserDetails();
}

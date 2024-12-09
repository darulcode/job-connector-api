package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.ChangePasswordRequest;
import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getOne(String id);
    UserResponse create(UserRequest userRequest);
    User findByUsername(String username);
    UserResponse update(String id, UserRequest userRequest);
    void delete(String id);
    Page<UserResponse> findAllUser(UserSearchRequest userSearchRequest);
    UserResponse getUserDetails(String id);
    UserResponse getSelfUserDetails();
    void changePassword(ChangePasswordRequest request);
}

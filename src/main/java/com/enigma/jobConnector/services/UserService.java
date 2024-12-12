package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.ChangePasswordRequest;
import com.enigma.jobConnector.dto.request.ForgotPasswordRequest;
import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.dto.response.ImportUserResponse;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {

    User getOne(String id);
    UserResponse create(UserRequest userRequest);
    UserResponse update(String id, UserRequest userRequest);
    void delete(String id);
    Page<UserResponse> findAllUser(UserSearchRequest userSearchRequest);
    ImportUserResponse batchCreate(List<User> users);
    List<User> findAll();
    UserResponse getUserDetails(String id);
    UserResponse getSelfUserDetails();
    void changePassword(ChangePasswordRequest request);
    void sendForgotPassword(String email) throws MessagingException, IOException;
    void checkValidCode(String code);
    void changePassword(ForgotPasswordRequest request);
}

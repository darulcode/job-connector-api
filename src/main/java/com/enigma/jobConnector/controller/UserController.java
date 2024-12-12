package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.ChangePasswordRequest;
import com.enigma.jobConnector.dto.request.ForgotPasswordRequest;
import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.services.UserService;
import com.enigma.jobConnector.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(Constant.USER_API)
@RequiredArgsConstructor
@Tag(name = "User", description = "APIs for create, update, delete and get user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "create user")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.create(userRequest);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_USER, userResponse);
    }

    @Operation(summary = "update user")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,@RequestBody UserRequest userRequest) {
        UserResponse response = userService.update(id , userRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_USER, response);
    }

    @Operation(summary = "delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_USER, null);
    }

    @Operation(summary = "get self detail")
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        UserResponse response = userService.getSelfUserDetails();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_USER, response);
    }

    @Operation(summary = "get all user")
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "name", required = false) String query) {

        UserSearchRequest userSearchRequest = UserSearchRequest.builder()
                .query(query)
                .sortBy(sortBy)
                .size(size)
                .page(page)
                .build();
        Page<UserResponse> response = userService.findAllUser(userSearchRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_FETCHING_ALL_USER, response);
    }

    @Operation(summary = "change password")
    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_CHANGE_PASSWORD, null);
    }

    @Operation(summary = "get user")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        UserResponse userDetails = userService.getUserDetails(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_USER, userDetails);
    }

    @Operation(summary = "Send forgot password to email user")
    @PostMapping("  ")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) throws MessagingException, IOException {
        userService.sendForgotPassword(email);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_SEND_FORGOT_PASSWORD, null);
    }

    @Operation(summary = "Valide code forgot password")
    @GetMapping("/verify/{code}")
    public ResponseEntity<?> verifyCode(@PathVariable String code) {
        userService.checkValidCode(code);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.CODE_IS_VALID, null);
    }

    @Operation(summary = "Change password")
    @PostMapping("/forgot")
    public ResponseEntity<?> changePassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        userService.changePassword(forgotPasswordRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_CHANGE_PASSWORD, null);
    }

}

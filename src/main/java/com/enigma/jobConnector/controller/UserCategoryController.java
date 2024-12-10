package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.services.UserCategoryService;
import com.enigma.jobConnector.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(Constant.USER_CATEGORY_API)
@Tag(name = "User category", description = "APIs for create, update, delete and get user category")
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    @Operation(summary = "create user category")
    @PostMapping
    public ResponseEntity<?> createUserCategory(@RequestBody UserCategoryRequest request) {
        UserCategoryResponse response = userCategoryService.createUserCategory(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_USER_CATEGORY, response);
    }

    @Operation(summary = "update user category")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserCategory(@PathVariable String id, @RequestBody UserCategoryRequest request) {
        UserCategoryResponse response = userCategoryService.updateUserCategory(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_USER_CATEGORY, response);
    }

    @Operation(summary = "get all user category")
    @GetMapping
    public ResponseEntity<?> getAllUserCategory() {
        List<UserCategoryResponse> response = userCategoryService.getAllUserCategories();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_ALL_USER_CATEGORY, response);
}

    @Operation(summary = "get user category")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserCategory(@PathVariable String id) {
        UserCategoryResponse response = userCategoryService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_USER_CATEGORY, response);
    }

    @Operation(summary = "delete user category")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserCategory(@PathVariable String id) {
        userCategoryService.delete(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_USER_CATEGORY, null);
    }


}

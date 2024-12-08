package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.dto.request.UserCategorySearchRequest;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.entity.UserCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserCategoryService {

    UserCategoryResponse createUserCategory(UserCategoryRequest userCategoryRequest);
    UserCategoryResponse updateUserCategory(String id, UserCategoryRequest userCategoryRequest);
    List<UserCategoryResponse> getAllUserCategories();
    UserCategory getOne(String id);
    UserCategoryResponse getById(String id);
    void delete(String id);
    UserCategory getByName(String name);
}

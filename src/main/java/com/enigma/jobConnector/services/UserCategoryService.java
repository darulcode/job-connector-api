package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.dto.request.UserCategorySearchRequest;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.entity.UserCategory;
import org.springframework.data.domain.Page;

public interface UserCategoryService {

    UserCategoryResponse createUserCategory(UserCategoryRequest userCategoryRequest);
    UserCategoryResponse updateUserCategory(String id, UserCategoryRequest userCategoryRequest);
    Page<UserCategoryResponse> getAllUserCategories(UserCategorySearchRequest request);
    UserCategory getOne(String id);
    UserCategoryResponse getById(String id);
    void delete(String id);
    UserCategory getByName(String name);
}

package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.dto.request.UserCategorySearchRequest;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.entity.UserCategory;
import com.enigma.jobConnector.repository.UserCategoryRepository;
import com.enigma.jobConnector.services.UserCategoryService;
import com.enigma.jobConnector.specification.UserCategorySpecification;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCategoryServiceImpl implements UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserCategoryResponse createUserCategory(UserCategoryRequest userCategoryRequest) {
        AuthenticationContextUtil.validateCurrentUserRoleSuperAdmin();
        UserCategory category = getByName(userCategoryRequest.getName());
        if (category != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.CATEGORY_NAME_ALREADY_EXIST);
        }
        UserCategory userCategory = UserCategory.builder()
                .name(userCategoryRequest.getName())
                .build();
        userCategoryRepository.saveAndFlush(userCategory);
        return getUserCategoryResponse(userCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserCategoryResponse updateUserCategory(String id, UserCategoryRequest userCategoryRequest) {
        AuthenticationContextUtil.validateCurrentUserRoleSuperAdmin();
        UserCategory user = getOne(id);
        user.setName(userCategoryRequest.getName());
        userCategoryRepository.saveAndFlush(user);
        return getUserCategoryResponse(user);
    }

    @Override
    public Page<UserCategoryResponse> getAllUserCategories(UserCategorySearchRequest request) {
        Sort sortBy = ShortUtil.parseSort(request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Specification<UserCategory> specification = UserCategorySpecification.getSpecification(request);
        Page<UserCategory> response = userCategoryRepository.findAll(specification, pageable);
        return response.map(this::getUserCategoryResponse);
    }


    @Override
    public UserCategory getOne(String id) {
        return userCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.CATEGORY_NOT_FOUND));
    }

    @Override
    public UserCategoryResponse getById(String id) {
        return getUserCategoryResponse(getOne(id));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        AuthenticationContextUtil.validateCurrentUserRoleSuperAdmin();
        userCategoryRepository.delete(getOne(id));
    }

    @Override
    public UserCategory getByName(String name) {
        Optional<UserCategory> userCategory = userCategoryRepository.findByName(name);
        return userCategory.orElse(null);
    }


    private UserCategoryResponse getUserCategoryResponse(UserCategory userCategory) {
        return UserCategoryResponse.builder()
                .id(userCategory.getId())
                .name(userCategory.getName())
                .build();
    }
}

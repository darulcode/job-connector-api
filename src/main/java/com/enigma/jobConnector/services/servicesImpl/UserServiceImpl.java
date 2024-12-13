package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.constants.UserStatus;
import com.enigma.jobConnector.dto.request.ChangePasswordRequest;
import com.enigma.jobConnector.dto.request.ForgotPasswordRequest;
import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.dto.response.FailedImportUserResponse;
import com.enigma.jobConnector.dto.response.ImportUserResponse;
import com.enigma.jobConnector.dto.response.UserCategoryResponse;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.entity.UserCategory;
import com.enigma.jobConnector.repository.UserRepository;
import com.enigma.jobConnector.services.EmailService;
import com.enigma.jobConnector.services.UserCategoryService;
import com.enigma.jobConnector.services.UserService;
import com.enigma.jobConnector.specification.UserSpecification;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import com.enigma.jobConnector.utils.ShortUtil;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.enigma.jobConnector.utils.AuthenticationContextUtil.validateCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCategoryService userCategoryService;
    private final EmailService emailService;

    @PostConstruct
    public void init(){
        User user = User.builder()
                .name("Super Admin")
                .email("superadmin@enigma.com")
                .phoneNumber("+62 82342343287432")
                .role(UserRole.ROLE_SUPER_ADMIN)
                .password(passwordEncoder.encode("password"))
                .build();

        if (userRepository.findByEmail("superadmin@enigma.com").isPresent()) {return;}
        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse create(UserRequest userRequest) {
        AuthenticationContextUtil.validateCurrentUserRoleSuperAdmin();

        UserCategory userCategory = null;
        if (userRequest.getCategoryId() != null && !userRequest.getCategoryId().isEmpty()) {
            userCategory = userCategoryService.getOne(userRequest.getCategoryId());
        }

        Optional<User> userResult = userRepository.findByEmail(userRequest.getEmail());
        if (userResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USERNAME_OR_EMAIL_ALREADY_EXIST);
        }
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .userCategory(userCategory)
                .role(UserRole.fromDescription(userRequest.getRole()))
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .status(UserStatus.AKTIVE)
                .build();
        userRepository.saveAndFlush(user);
        return getUserResponse(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse update(String id, UserRequest userRequest) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        if (!currentUser.getRole().equals(UserRole.ROLE_SUPER_ADMIN) && !currentUser.getId().equals(id)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        User user = getOne(id);
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        userRepository.saveAndFlush(user);
        return getUserResponse(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        if (!currentUser.getRole().equals(UserRole.ROLE_SUPER_ADMIN) ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        User user = getOne(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponse> findAllUser(UserSearchRequest request) {
        Sort sortBy = ShortUtil.parseSort(request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Specification<User> userSpecification = UserSpecification.getSpecification(request);

        Page<User> users = userRepository.findAll(userSpecification,pageable);
        return users.map(this::getUserResponse);
    }

    @Override
    public Boolean isUserExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchCreate(List<User> users) {
        userRepository.saveAll(users);
    }


    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserDetails(String id) {
        User user = getOne(id);
        return getUserResponse(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getSelfUserDetails() {
        validateCurrentUser();
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        return getUserResponse(getOne(currentUser.getId()));
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_CREDENTIAL);
        }
        if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.PASSWORD_SAME);
        }
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    @Override
    public void sendForgotPassword(String email) throws MessagingException, IOException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_CREDENTIAL));
        //TODO: generate jwt or something for code forgot user
        String uuid = String.valueOf(UUID.randomUUID());
        user.setCode(uuid);
        userRepository.save(user);
        emailService.sendEmail(email, "Forgot Password Url", "google.com/"+uuid);
    }

    @Override
    public void checkValidCode(String code) {
        userRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_CREDENTIAL));
    }

    @Override
    public void changePassword(ForgotPasswordRequest request) {
        User user = userRepository.findByCode(request.getCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_CREDENTIAL));
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.PASSWORD_SAME);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setCode(null);
        userRepository.save(user);
    }

    @Override
    public User getOne(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USER_NOT_FOUND));
    }

    private UserResponse getUserResponse(User user) {
        UserCategoryResponse category = null;
        if (user.getUserCategory() !=  null){
            UserCategory userCategoryResult = user.getUserCategory();
            category = UserCategoryResponse.builder()
                    .id(userCategoryResult.getId())
                    .name(userCategoryResult.getName())
                    .build();
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .category(category)
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole().getDescription())
                .build();
    }

}

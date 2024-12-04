package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.dto.request.UserRequest;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.dto.response.UserResponse;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.UserRepository;
import com.enigma.jobConnector.services.UserService;
import com.enigma.jobConnector.spesification.UserSpecification;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import com.enigma.jobConnector.utils.ShortUtil;
import jakarta.annotation.PostConstruct;
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

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        User user = User.builder()
                .name("Super Admin")
                .email("superadmin@enigma.com")
                .username("superadmin")
                .role(UserRole.ROLE_SUPER_ADMIN)
                .password(passwordEncoder.encode("password"))
                .build();

        if (userRepository.findByUsername("superadmin").isPresent()) {return;}
        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse create(UserRequest userRequest) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        log.info("Creating user {}", userRequest.getUsername());
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        if (!currentUser.getRole().equals(UserRole.ROLE_SUPER_ADMIN) ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);

        Optional<User> userResult = userRepository.findByEmailOrUsername(userRequest.getEmail(), userRequest.getUsername());
        // TODO: username my be generate by name user
        if (userResult.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USERNAME_OR_EMAIL_ALREADY_EXIST);
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .username(userRequest.getUsername())
                .role(UserRole.fromDescription(userRequest.getRole()))
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        userRepository.saveAndFlush(user);
        return getUserResponse(user);

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USERNAME_NOT_FOUND));
    }

    @Override
    public UserResponse update(UserRequest userRequest) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        if (!currentUser.getRole().equals(UserRole.ROLE_SUPER_ADMIN) || currentUser.getId().equals(userRequest.getId())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        User user = getOne(userRequest.getId());
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRole(UserRole.fromDescription(userRequest.getRole()));
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
        return getUserResponse(user);
    }

    @Override
    public void delete(String id) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        if (!currentUser.getRole().equals(UserRole.ROLE_SUPER_ADMIN) ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        userRepository.delete(getOne(id));
    }

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
    public UserResponse getUserDetails(String id) {
        User user = getOne(id);
        return getUserResponse(user);
    }

    @Override
    public User getOne(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.USER_NOT_FOUND));
    }

    private UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getDescription())
                .build();
    }
}

package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateSuperAdminUserWhenInit() {
        User mockUser = User.builder()
                .name("Super Admin")
                .email("superadmin@enigma.com")
                .username("superadmin")
                .role(UserRole.ROLE_SUPER_ADMIN)
                .password("encodedPassword")
                .build();

        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.findByUsername("superadmin")).thenReturn(java.util.Optional.empty());

        userService.init();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(mockUser.getName(), capturedUser.getName());
        assertEquals(mockUser.getEmail(), capturedUser.getEmail());
        assertEquals(mockUser.getUsername(), capturedUser.getUsername());
        assertEquals(mockUser.getRole(), capturedUser.getRole());
        assertEquals(mockUser.getPassword(), capturedUser.getPassword());
    }

    @Test
    void shouldDoNothingWhenInit() {
        Mockito.when(userRepository.findByUsername("superadmin")).thenReturn(java.util.Optional.of(new User()));

        userService.init();

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    void shouldReturnUserWhenGetOne() {
        User mockUserAccount = User.builder()
                .id("1")
                .username("username")
                .role(UserRole.ROLE_ADMIN)
                .build();
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(mockUserAccount));

        User user = userService.getOne("1");

        assertEquals(mockUserAccount.getId(), user.getId());
        assertEquals(mockUserAccount.getUsername(), user.getUsername());
        assertEquals(mockUserAccount.getRole(), user.getRole());
    }

    @Test
    void shouldReturnUserDetailsWhenLoadUserByUsername() {
        String username = "username";

        User mockUser = User.builder()
                .username(username)
                .password("encodedPassword")
                .role(UserRole.ROLE_ADMIN)
                .build();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertEquals(mockUser.getUsername(), userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
    }
}
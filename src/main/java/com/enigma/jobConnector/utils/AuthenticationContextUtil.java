package com.enigma.jobConnector.utils;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class AuthenticationContextUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("current user: {}", authentication.getPrincipal());
        if (authentication.getPrincipal()== "anonymousUser") return null;
        return (User) authentication.getPrincipal();
    }

    public static void validateCurrentUser() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
    }

    public static void validateCurrentUserRoleSuperAdmin() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
        if (!UserRole.ROLE_SUPER_ADMIN.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
    }

}

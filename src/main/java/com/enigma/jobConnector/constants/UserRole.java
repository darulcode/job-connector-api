package com.enigma.jobConnector.constants;


import lombok.Getter;

@Getter
public enum UserRole {

    ROLE_SUPER_ADMIN("SuperAdmin"),
    ROLE_ADMIN("Admin"),
    ROLE_TRAINEE("Trainee");

    private String description;

    UserRole(String description) {
        this.description = description;
    }

    public static UserRole fromDescription(String description) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.description.equals(description)) {
                return userRole;
            }
        }
        return null;
    }
}


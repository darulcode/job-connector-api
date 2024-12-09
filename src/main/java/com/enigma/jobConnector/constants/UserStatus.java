package com.enigma.jobConnector.constants;

import lombok.Getter;

@Getter
public enum UserStatus {
    AKTIVE("Aktive"),
    INACTIVE("Inactive");

    private String description;

    UserStatus(String description) {
        this.description = description;
    }

    public static UserStatus fromDescription(String description) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.description.equals(description)) {
                return userStatus;
            }
        }
        return null;
    }
}
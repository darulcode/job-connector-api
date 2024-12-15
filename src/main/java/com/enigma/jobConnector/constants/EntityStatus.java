package com.enigma.jobConnector.constants;

import lombok.Getter;

@Getter
public enum EntityStatus {
    AKTIVE("Aktive"),
    INACTIVE("Inactive");

    private String description;

    EntityStatus(String description) {
        this.description = description;
    }

    public static EntityStatus fromDescription(String description) {
        for (EntityStatus entityStatus : EntityStatus.values()) {
            if (entityStatus.description.equals(description)) {
                return entityStatus;
            }
        }
        return null;
    }
}
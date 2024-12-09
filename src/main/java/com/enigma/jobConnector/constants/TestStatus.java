package com.enigma.jobConnector.constants;

import lombok.Getter;

@Getter
public enum TestStatus {
    PENDING("Pending"),
    FINISH("Finish"),
    AWAITING("Awaiting"),
    CANCEL("Cancel");

    private String description;

    TestStatus(String description) {
        this.description = description;
    }

    public static TestStatus fromDescription(String description) {
        for (TestStatus testStatus : TestStatus.values()) {
            if (testStatus.description.equals(description)) {
                return testStatus;
            }
        }
        return null;
    }
}

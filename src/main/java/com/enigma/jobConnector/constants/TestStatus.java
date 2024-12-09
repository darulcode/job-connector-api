package com.enigma.jobConnector.constants;

import lombok.Getter;

@Getter
public enum TestStatus {
    PENDING("Pending"),
    FINISH("Finish"),
    SUBMITTED("Submitted"), // submit tapi belum deadline
    AWAITING("Awaiting"), // sudah deadline
    CANCEL("Cancel"); // tidak jadi sama sekali dari client

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

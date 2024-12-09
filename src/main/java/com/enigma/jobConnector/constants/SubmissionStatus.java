package com.enigma.jobConnector.constants;

import lombok.Getter;

@Getter
public enum SubmissionStatus {
    PENDING("Pending"),
    REJECT("Reject"),
    SUBMITTED("Submitted"),
    ACCEPT("Accept");

    private String description;

    SubmissionStatus(String description) {
        this.description = description;
    }

    public static SubmissionStatus fromDescription(String description) {
        for (SubmissionStatus submissionStatus : SubmissionStatus.values()) {
            if (submissionStatus.description.equals(description)) {
                return submissionStatus;
            }
        }
        return null;
    }
}

package com.enigma.jobConnector.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDetailResponse {
    private String id;
    private String userId;
    private String status;
    private String submissionText;
    private String fileSubmission;
}

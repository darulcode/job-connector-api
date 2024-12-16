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
    private String name;
    private String category;
    private String status;
    private String submissionText;
    private String fileSubmission;
    private String fileName;
}

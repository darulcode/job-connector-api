package com.enigma.jobConnector.dto.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResponse {

    private String id;
    private String createdAt;
    private String deadlineAt;
    private String client;
    private String clientId;
    private FileTestResponse file;
    private String admin;
    private String description;
    private String status;
    private List<TestDetailResponse> testDetail;
}

package com.enigma.jobConnector.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestRequest {
    private LocalDateTime deadlineAt;
    private String clientId;
    private String description;
    private String status;

}

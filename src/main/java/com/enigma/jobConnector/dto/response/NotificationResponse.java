package com.enigma.jobConnector.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String id;
    private String body;
    private String screen;
    private Boolean isRead;
    private String testId;
    private String userId;
}

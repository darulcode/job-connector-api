package com.enigma.jobConnector.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationTokenRequest {
    private String notificationToken;
}

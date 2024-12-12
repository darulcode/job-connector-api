package com.enigma.jobConnector.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushNotificationRequest {

    @JsonProperty(value = "to")
    String notificationToken;

    String title;
    String body;
    PushNotificationDataRequest data;
}

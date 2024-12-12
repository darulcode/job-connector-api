package com.enigma.jobConnector.client;

import com.enigma.jobConnector.dto.request.PushNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "push-notification", url = "https://exp.host")
public interface PushNotificationFeignClient {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            path = "/--/api/v2/push/send"
    )
    void pushNotification(@RequestBody List<PushNotificationRequest> request);
}

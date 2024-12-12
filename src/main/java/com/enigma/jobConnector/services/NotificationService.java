package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.PagingAndShortingRequest;
import com.enigma.jobConnector.dto.response.NotificationResponse;
import com.enigma.jobConnector.entity.Client;
import com.enigma.jobConnector.entity.Test;
import org.springframework.data.domain.Page;

public interface NotificationService {
    void createBatchNotificationTest(Client client, Test test);
    Page<NotificationResponse> getNotification(PagingAndShortingRequest pagingAndShortingRequest);
    NotificationResponse readNotification(String notificationId);
}

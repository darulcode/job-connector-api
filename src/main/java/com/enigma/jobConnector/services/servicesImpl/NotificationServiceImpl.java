package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.client.PushNotificationFeignClient;
import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.PagingAndShortingRequest;
import com.enigma.jobConnector.dto.request.PushNotificationDataRequest;
import com.enigma.jobConnector.dto.request.PushNotificationRequest;
import com.enigma.jobConnector.dto.response.NotificationResponse;
import com.enigma.jobConnector.entity.*;
import com.enigma.jobConnector.repository.NotificationRepository;
import com.enigma.jobConnector.services.NotificationService;
import com.enigma.jobConnector.services.TestDetailService;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import com.enigma.jobConnector.utils.ShortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PushNotificationFeignClient pushNotificationFeignClient;
    private final TestDetailService testDetailService;

    @Override
    public void createBatchNotificationTest(Client client, Test test) {
        List<PushNotificationRequest> pushNotificationRequests = new ArrayList<>();

        PushNotificationDataRequest pushNotificationDataRequest = PushNotificationDataRequest.builder()
                .screen("Test")
                .testId(test.getId())
                .build();

        List<TestDetail> testDetails = testDetailService.listFindByTestId(test.getId());
        for (TestDetail testDetail : testDetails) {
            User user = testDetail.getUser();

            for (NotificationToken notificationToken : user.getNotificationTokens()) {
                PushNotificationRequest pushNotificationRequest = PushNotificationRequest.builder()
                        .notificationToken(notificationToken.getNotificationToken())
                        .title(String.format(Constant.NOTIFICATION_TITLE_FORMAT, client.getName()))
                        .body(test.getDescription())
                        .data(pushNotificationDataRequest)
                        .build();

                Notification notification = Notification.builder()
                        .title(String.format(Constant.NOTIFICATION_TITLE_FORMAT, client.getName()))
                        .body(test.getDescription())
                        .screen(pushNotificationDataRequest.getScreen())
                        .isRead(false)
                        .test(test)
                        .user(user)
                        .build();

                notificationRepository.save(notification);
                pushNotificationRequests.add(pushNotificationRequest);
            }
        }

        pushNotificationFeignClient.pushNotification(pushNotificationRequests);
    }

    @Override
    public Page<NotificationResponse> getNotification(PagingAndShortingRequest request) {
        User user = AuthenticationContextUtil.getCurrentUser();
        Sort sortBy = ShortUtil.parseSort(request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Page<Notification> notifications = notificationRepository.findAllByUserId(user.getId(), pageable);
        return notifications.map(this::getNotificationResponse);
    }

    @Override
    public NotificationResponse readNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.NOTIFICATION_NOT_FOUND));
        notification.setIsRead(true);
        return getNotificationResponse(notificationRepository.saveAndFlush(notification));
    }

    private NotificationResponse getNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .body(notification.getBody())
                .screen(notification.getScreen())
                .isRead(notification.getIsRead())
                .testId(notification.getTest().getId())
                .userId(notification.getUser().getId())
                .build();
    }
}

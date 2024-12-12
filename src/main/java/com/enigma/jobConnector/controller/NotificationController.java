package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.PagingAndShortingRequest;
import com.enigma.jobConnector.dto.response.NotificationResponse;
import com.enigma.jobConnector.services.NotificationService;
import com.enigma.jobConnector.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotification(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        PagingAndShortingRequest pagingAndShortingRequest = PagingAndShortingRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        Page<NotificationResponse> responses = notificationService.getNotification(pagingAndShortingRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_FETCHING_NOTIFICATION, responses);
    }

    @GetMapping("/read/{notificationId}")
    public ResponseEntity<?> readNotification(@PathVariable String notificationId) {
        NotificationResponse response = notificationService.readNotification(notificationId);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_SET_READ_NOTIFICATION, response);
    }
}

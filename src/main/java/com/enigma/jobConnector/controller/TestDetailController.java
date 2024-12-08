package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.services.TestDetailService;
import com.enigma.jobConnector.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constant.TEST_DETAIL_API)
@RequiredArgsConstructor
public class TestDetailController {

    private final TestDetailService testDetailService;

    @GetMapping
    public ResponseEntity<?> getAllTestDetails() {
        List<TestResponse> responses = testDetailService.findAllByUserId();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETHING_TEST_DETAILS, responses);
    }
}

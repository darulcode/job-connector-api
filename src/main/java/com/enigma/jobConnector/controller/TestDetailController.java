package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.UpdateStatusSubmissionRequest;
import com.enigma.jobConnector.dto.request.UpdateTestDetailRequest;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.services.TestDetailService;
import com.enigma.jobConnector.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(Constant.TEST_DETAIL_API)
@RequiredArgsConstructor
public class TestDetailController {

    private final TestDetailService testDetailService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> getAllTestDetails() {
        List<TestResponse> responses = testDetailService.findAllTestByUser();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_TEST_DETAILS, responses);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> postTestDetail(@PathVariable String id,
                                            @RequestPart(name = "file", required = false) MultipartFile multipartFiles,
                                            @RequestPart(name = "test") String request) {
        try {
            UpdateTestDetailRequest testDetailRequest = objectMapper.readValue(request, UpdateTestDetailRequest.class);
            TestDetailResponse testDetailResponse = testDetailService.submitSubmission(id, testDetailRequest, multipartFiles);
            return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_SUBMITTED_SUBMISSION, testDetailResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStatusSubmission(
            @PathVariable String id,
            @RequestBody UpdateStatusSubmissionRequest request
            ) {
        TestDetailResponse testDetailResponse = testDetailService.updateStatusSubmission(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_CHANGE_STATUS_SUBMISSION, testDetailResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTestDetail(@PathVariable String id) {
        TestResponse response = testDetailService.findById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_TEST_DETAILS, response);
    }
}

package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.ChangeStatusTestRequest;
import com.enigma.jobConnector.dto.request.TestRequest;
import com.enigma.jobConnector.dto.request.TestSearchRequest;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.services.TestService;
import com.enigma.jobConnector.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping(Constant.TEST_API)
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "file", required = false) MultipartFile multipartFiles,
                                    @RequestPart(name = "test") String request) {
        try {
            TestRequest testRequest = objectMapper.readValue(request, TestRequest.class);
            TestResponse testResponse = testService.create(testRequest, multipartFiles);
            return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_TEST_MESSAGE, testResponse);

        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id,
                                    @RequestPart(name = "file", required = false) MultipartFile multipartFiles,
                                    @RequestPart(name = "test") String request
                                    ) {
        try {
            TestRequest testRequest = objectMapper.readValue(request, TestRequest.class);
            TestResponse testResponse = testService.updateTest(id,testRequest, multipartFiles);
            return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_UPDATE_TEST_MESSAGE, testResponse);

        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") String id, @RequestBody ChangeStatusTestRequest request) {
        testService.changeTestStatus(id,request.getStatus());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_TEST_MESSAGE, null);
    }

    @GetMapping
    public ResponseEntity<?> getAllTests(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "name", required = false) String query
    ) {
        TestSearchRequest request = TestSearchRequest.builder()
                .query(query)
                .sortBy(sortBy)
                .size(size)
                .page(page)
                .build();
        Page<TestResponse> responses = testService.getAll(request);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_FETCHING_ALL_TEST, responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        TestResponse response = testService.findById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_FETCHING_TEST, response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        testService.deleteTest(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_TEST, null);
    }
}

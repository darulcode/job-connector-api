package com.enigma.jobConnector.controller;


import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.request.TestRequest;
import com.enigma.jobConnector.dto.request.TestSearchRequest;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.services.TestService;
import com.enigma.jobConnector.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constant.TEST_API)
@RequiredArgsConstructor
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
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

    @GetMapping
    public ResponseEntity<?> getAllTests(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "name", required = false) String query,
            @RequestParam(name = "admin", required = false) String admin,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "client", required = false) String client
    ) {
        log.info("Masuk ke test search request");
        TestSearchRequest request = TestSearchRequest.builder()
                .query(query)
                .sortBy(sortBy)
                .size(size)
                .page(page)
                .admin(admin)
                .status(status)
                .client(client)
                .build();
        Page<TestResponse> responses = testService.getAll(request);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_FETCHING_ALL_TEST, responses);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        testService.deleteTest(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_TEST, null);
    }
}

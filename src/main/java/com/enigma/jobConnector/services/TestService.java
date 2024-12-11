package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.TestRequest;
import com.enigma.jobConnector.dto.request.TestSearchRequest;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TestService {
    Test getOne(String id);
    TestResponse create(TestRequest test, MultipartFile file) throws IOException;
    Page<TestResponse> getAll(TestSearchRequest testSearchRequest);
    void deleteTest(String id);
    TestResponse updateTest(String id, TestRequest test, MultipartFile file) throws IOException;
    void changeTestStatus(String id, String status);
    TestResponse findById(String id);
}

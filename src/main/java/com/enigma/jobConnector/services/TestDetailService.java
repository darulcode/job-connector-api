package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.TestDetailRequest;
import com.enigma.jobConnector.dto.request.UpdateStatusSubmissionRequest;
import com.enigma.jobConnector.dto.request.UpdateTestDetailRequest;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.TestDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TestDetailService {
    TestDetail create(TestDetailRequest testDetails, Test test);
    List<TestDetailResponse> findByTestId(String testId);
    TestDetail getOne(String id);
    TestResponse findById(String id);
    List<TestResponse> findAllTestByUser();
    TestDetailResponse submitSubmission(String id, UpdateTestDetailRequest request, MultipartFile file) throws IOException;
    TestDetailResponse updateStatusSubmission(String id, UpdateStatusSubmissionRequest request);
    void addTrainee(Test test, String userId);
}

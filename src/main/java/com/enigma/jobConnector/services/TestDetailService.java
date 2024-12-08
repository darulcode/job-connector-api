package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.request.TestDetailRequest;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.TestDetail;

import java.util.List;

public interface TestDetailService {
    TestDetail create(TestDetailRequest testDetails, Test test);
    List<TestDetailResponse> findByTestId(String testId);
    TestDetail getOne(String id);
    TestDetailResponse findById(String id);
    List<TestResponse> findAllByUserId();
    TestDetailResponse update(String id, TestDetailRequest testDetailRequest);
}

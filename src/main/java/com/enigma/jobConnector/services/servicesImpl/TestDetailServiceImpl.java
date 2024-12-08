package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.SubmissionStatus;
import com.enigma.jobConnector.dto.request.TestDetailRequest;
import com.enigma.jobConnector.dto.response.FileTestResponse;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.TestDetail;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.TestDetailRepository;
import com.enigma.jobConnector.services.TestDetailService;
import com.enigma.jobConnector.services.UserService;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDetailServiceImpl implements TestDetailService {
    private final TestDetailRepository testDetailRepository;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestDetail create(TestDetailRequest testDetails, Test test) {
        User user = userService.getOne(testDetails.getUserId());
        TestDetail testDetail = TestDetail.builder()
                .user(user)
                .test(test)
                .status(SubmissionStatus.PENDING)
                .build();

        return testDetailRepository.saveAndFlush(testDetail);
    }

    @Override
    public List<TestDetailResponse> findByTestId(String testId) {
        List<TestDetail> testDetails = testDetailRepository.findAllByTestId(testId);
        return testDetails.stream().map(this::getTestDetailResponse).toList();
    }

    @Override
    public TestDetail getOne(String id) {
        return testDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test detail not found"));
    }

    @Override
    public TestDetailResponse findById(String id) {
        return getTestDetailResponse(getOne(id));
    }

    @Override
    public List<TestResponse> findAllByUserId() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        List<TestDetail> testDetails = testDetailRepository.findAllByUserId(currentUser.getId());
        return testDetails.stream().map(this::getTestResponse).toList();
    }

    @Override
    public TestDetailResponse update(String id, TestDetailRequest testDetailRequest) {
        return null;
    }


    private TestDetailResponse getTestDetailResponse(TestDetail testDetail) {
        String fileSubmission = null;
        if (testDetail.getFileSubmissionTest() != null) {
            fileSubmission = "/api/file/"+testDetail.getFileSubmissionTest().getFile().getId();
        }
        return TestDetailResponse.builder()
                .id(testDetail.getId())
                .userId(testDetail.getUser().getId())
                .status(testDetail.getStatus().getDescription())
                .submissionText(testDetail.getSubmissionText())
                .fileSubmission(fileSubmission)
                .build();
    }

    private TestResponse getTestResponse(TestDetail testDetail) {
        Test test = testDetail.getTest();
        FileTestResponse fileTestResponse = new FileTestResponse(null, null);
        if (test.getFileTest() != null) {
            fileTestResponse.setUrlFIle("/api/file/"+ test.getFileTest().getFile().getId());
            fileTestResponse.setFileName(test.getFileTest().getFile().getName());
        }
        TestDetailResponse testDetailResponse = getTestDetailResponse(testDetail);
        return TestResponse.builder()
                .id(test.getId())
                .createdAt(test.getCreatedAt().toString())
                .description(test.getDescription())
                .deadlineAt(test.getDeadlineAt().toString())
                .client(test.getClient().getName())
                .admin(test.getUser().getName())
                .file(fileTestResponse)
                .status(test.getStatus().getDescription())
                .testDetail(List.of(testDetailResponse))
                .build();
    }
}

package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.SubmissionStatus;
import com.enigma.jobConnector.constants.TestStatus;
import com.enigma.jobConnector.dto.request.TestDetailRequest;
import com.enigma.jobConnector.dto.request.UpdateStatusSubmissionRequest;
import com.enigma.jobConnector.dto.request.UpdateTestDetailRequest;
import com.enigma.jobConnector.dto.response.FileTestResponse;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.entity.FileSubmissionTest;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.TestDetail;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.TestDetailRepository;
import com.enigma.jobConnector.services.FileService;
import com.enigma.jobConnector.services.FileSubmissionService;
import com.enigma.jobConnector.services.TestDetailService;
import com.enigma.jobConnector.services.UserService;
import com.enigma.jobConnector.specification.TestDetailSpecification;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDetailServiceImpl implements TestDetailService {
    private final TestDetailRepository testDetailRepository;
    private final UserService userService;
    private final FileSubmissionService fileSubmissionService;
    private final FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestDetail create(String testDetails, Test test) {
        User user = userService.getOne(testDetails);
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
        return testDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.TEST_DETAIL_NOT_FOUND));
    }

    @Override
    public TestResponse findById(String id) {
        return getTestResponse(getOne(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TestResponse> findAllTestByUser() {
        AuthenticationContextUtil.validateCurrentUser();
        updateExpiredTestsToAwaiting();
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        Specification<TestDetail> specification = TestDetailSpecification.getSpecification(currentUser.getId());
        List<TestDetail> testDetails = testDetailRepository.findAll(specification );
        return testDetails.stream().map(this::getTestResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestDetailResponse submitSubmission(String id, UpdateTestDetailRequest request, MultipartFile file) throws IOException {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        TestDetail testDetail = getOne(id);
        if (!currentUser.getId().equals(testDetail.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.UNAUTHORIZED_MESSAGE);
        }
        FileSubmissionTest fileSubmissionTest;
        if (file != null) {
            fileSubmissionTest = fileSubmissionService.createFileSubmission(file, testDetail);
            testDetail.setFileSubmissionTest(fileSubmissionTest);
            fileSubmissionTest.setTestDetail(testDetail);
        } else {
            testDetail.setFileSubmissionTest(null);
        }
        if (!request.getSubmissionText().isEmpty()) {
            testDetail.setSubmissionText(request.getSubmissionText());
        } else {
            testDetail.setSubmissionText(null);
        }
        testDetail.setStatus(SubmissionStatus.SUBMITTED);
        testDetailRepository.saveAndFlush(testDetail);
        return getTestDetailResponse(testDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestDetailResponse updateStatusSubmission(String id, UpdateStatusSubmissionRequest request) {
        AuthenticationContextUtil.validateCurrentUserRoleAdmin();
        TestDetail testDetail = getOne(id);
        SubmissionStatus submissionStatus = SubmissionStatus.fromDescription(request.getStatus());
        if (submissionStatus == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.STATUS_SUBMISSION_NOT_FOUND);
        }
        testDetail.setStatus(submissionStatus);
        testDetailRepository.saveAndFlush(testDetail);
        return getTestDetailResponse(testDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addTrainee(Test test, String userId) {
        Optional<TestDetail> testDetail = testDetailRepository.findByTestAndUserId(test, userId);

        if (testDetail.isEmpty()) {
            TestDetailRequest userRequest = TestDetailRequest.builder()
                    .userId(userId)
                    .build();
            create(userRequest.getUserId(), test);
        }
    }

    @Override
    public List<TestDetail> listFindByTestId(String testId) {
        return testDetailRepository.findAllByTestId(testId);
    }




    private TestDetailResponse getTestDetailResponse(TestDetail testDetail) {
        String fileSubmission = null;
        String fileName = null;
        if (testDetail.getFileSubmissionTest() != null) {
            fileSubmission = "/api/file/"+testDetail.getFileSubmissionTest().getFile().getId();
            fileName = testDetail.getFileSubmissionTest().getFile().getName();
        }
        return TestDetailResponse.builder()
                .id(testDetail.getId())
                .userId(testDetail.getUser().getId())
                .name(testDetail.getUser().getName())
                .category(testDetail.getUser().getUserCategory().getName())
                .status(testDetail.getStatus().getDescription())
                .submissionText(testDetail.getSubmissionText())
                .fileSubmission(fileSubmission)
                .fileName(fileName)
                .build();
    }

    private TestResponse getTestResponse(TestDetail testDetail) {
        Test test = testDetail.getTest();
        FileTestResponse fileTestResponse = new FileTestResponse(null, null);
        if (test.getFileTest() != null) {
            fileTestResponse.setUrlFile("/api/file/"+ test.getFileTest().getFile().getId());
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

    @Transactional
    public void updateExpiredTestsToAwaiting() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
        TestStatus currentStatus = TestStatus.PENDING;
        TestStatus newStatus = TestStatus.AWAITING;

        testDetailRepository.updateTestStatusToAwaiting(newStatus, now, currentStatus);
    }

}

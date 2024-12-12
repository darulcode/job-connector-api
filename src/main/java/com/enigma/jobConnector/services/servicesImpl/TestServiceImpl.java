package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.TestStatus;
import com.enigma.jobConnector.dto.request.TestRequest;
import com.enigma.jobConnector.dto.request.TestSearchRequest;
import com.enigma.jobConnector.dto.response.FileTestResponse;
import com.enigma.jobConnector.dto.response.TestDetailResponse;
import com.enigma.jobConnector.dto.response.TestResponse;
import com.enigma.jobConnector.entity.Client;
import com.enigma.jobConnector.entity.FileTest;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.repository.TestRepository;
import com.enigma.jobConnector.services.*;
import com.enigma.jobConnector.specification.TestSpecification;
import com.enigma.jobConnector.utils.AuthenticationContextUtil;
import com.enigma.jobConnector.utils.ShortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final ClientService clientService;
    private final FileTestService fileTestService;
    private final TestDetailService testDetailService;
    private final NotificationService notificationService;

    @Override
    public Test getOne(String id) {
        return testRepository.findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.TEST_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestResponse create(TestRequest request, MultipartFile file) throws IOException {
        User user = AuthenticationContextUtil.validateCurrentUserRoleAdmin();
        Client client = clientService.getOne(request.getClientId());

        FileTest fileTest = null;
        if (!file.isEmpty()) {
            fileTest = fileTestService.createFile(file);
        }
        Test test = Test.builder()
                .deadlineAt(request.getDeadlineAt())
                .description(request.getDescription())
                .fileTest(fileTest)
                .user(user)
                .client(client)
                .status(TestStatus.PENDING)
                .build();
        testRepository.saveAndFlush(test);
        if (fileTest != null) fileTest.setTest(test);
        request.getDetails().forEach(testDetailRequest -> testDetailService.create(testDetailRequest, test));
        notificationService.createBatchNotificationTest(client, getOne(test.getId()));
        return getTestResponse(test);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<TestResponse> getAll(TestSearchRequest request) {
        updateStatusIfPastDeadline();
        Sort sortBy = ShortUtil.parseSort(request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Specification<Test> specification = TestSpecification.getSpecification(request);
        Page<Test> response = testRepository.findAll(specification, pageable);

        return response.map(this::getTestResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTest(String id) {
        AuthenticationContextUtil.validateCurrentUserRoleAdmin();
        testRepository.delete(getOne(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestResponse updateTest(String id, TestRequest request, MultipartFile file) throws IOException {
        Test test = getOne(id);
        Client client = clientService.getOne(request.getClientId());

        if (file != null) {
            if (test.getFileTest() == null) {
                test.setFileTest(fileTestService.createFile(file));
                notificationService.createBatchNotificationTest(client, test);
            } else {
                test.setFileTest(fileTestService.udpdateFileTest(test, file));
                notificationService.createBatchNotificationTest(client, test);
            }
        }
        request.getDetails().forEach(trainee -> testDetailService.addTrainee(test, trainee.getUserId()));
        test.setDeadlineAt(request.getDeadlineAt());
        test.setDescription(request.getDescription());
        test.setClient(clientService.getOne(request.getClientId()));
        test.setStatus(TestStatus.PENDING);

        testRepository.save(test);
        notificationService.createBatchNotificationTest(client, test);
        return getTestResponse(test);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeTestStatus(String id, String status) {
        Test test = getOne(id);
        TestStatus testStatus = TestStatus.fromDescription(status);
        if (testStatus == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.TEST_STATUS_NOT_FOUND);
        test.setStatus(testStatus);
        testRepository.save(test);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestResponse findById(String id) {
        updateStatusIfPastDeadline();
        return getTestResponse(getOne(id));
    }

    private TestResponse getTestResponse(Test test) {
        List<TestDetailResponse> testDetailResponses = testDetailService.findByTestId(test.getId());
        FileTestResponse fileTestResponse = new FileTestResponse(null, null);
        if (test.getFileTest() != null) {
            fileTestResponse.setUrlFile("/api/file/"+ test.getFileTest().getFile().getId());
            fileTestResponse.setFileName(test.getFileTest().getFile().getName());
        }

        return TestResponse.builder()
                .id(test.getId())
                .createdAt(test.getCreatedAt().toString())
                .description(test.getDescription())
                .deadlineAt(test.getDeadlineAt().toString())
                .client(test.getClient().getName())
                .admin(test.getUser().getName())
                .file(fileTestResponse)
                .status(test.getStatus().getDescription())
                .testDetail(testDetailResponses)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatusIfPastDeadline() {
        testRepository.updateStatusToAwaiting(TestStatus.AWAITING, LocalDateTime.now(), TestStatus.PENDING);
    }


}







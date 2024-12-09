package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.dto.response.FileResponse;
import com.enigma.jobConnector.entity.File;
import com.enigma.jobConnector.entity.FileSubmissionTest;
import com.enigma.jobConnector.entity.TestDetail;
import com.enigma.jobConnector.repository.FileSubmissionTestRepository;
import com.enigma.jobConnector.services.FileService;
import com.enigma.jobConnector.services.FileSubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileSubmissionServiceImpl implements FileSubmissionService {
    private final FileSubmissionTestRepository fileSubmissionTestRepository;
    private final FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileSubmissionTest createFileSubmission(MultipartFile file,TestDetail testDetail) throws IOException {
        Optional<FileSubmissionTest> fileSubmissionTestResult = fileSubmissionTestRepository.findByTestDetail(testDetail);
        FileResponse fileResponse = fileService.uploadFile(file);
        File fileResult = fileService.getOne(fileResponse.getId());
        FileSubmissionTest fileSubmissionTest;
        if (fileSubmissionTestResult.isPresent()) {
            fileSubmissionTest = fileSubmissionTestResult.get();
            fileSubmissionTest.setFile(fileResult);
        } else {
            fileSubmissionTest= FileSubmissionTest.builder()
                    .file(fileResult)
                    .build();
        }
        return fileSubmissionTestRepository.saveAndFlush(fileSubmissionTest);
    }


}

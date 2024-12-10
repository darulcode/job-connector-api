package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.dto.response.FileResponse;
import com.enigma.jobConnector.entity.File;
import com.enigma.jobConnector.entity.FileTest;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.repository.FileTestRepository;
import com.enigma.jobConnector.services.FileService;
import com.enigma.jobConnector.services.FileTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileTestServiceImpl implements FileTestService {
    private final FileTestRepository fileTestRepository;
    private final FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileTest createFile(MultipartFile file) throws IOException {
        FileResponse fileResult = fileService.uploadFile(file);
        File fileResponse = fileService.getOne(fileResult.getId());
        FileTest fileTest = FileTest.builder()
                .file(fileResponse)
                .build();
        fileTestRepository.saveAndFlush(fileTest);
        return fileTest;
    }

    @Override
    public FileTest udpdateFileTest(Test test, MultipartFile file) throws IOException {
        FileTest fileTest = fileTestRepository.findByTest(test).orElse(null);
        FileResponse fileResponse = fileService.uploadFile(file);
        File fileResult = fileService.getOne(fileResponse.getId());
        if (fileTest == null) {
            FileTest fileTestResult = FileTest.builder()
                    .test(test)
                    .file(fileResult)
                    .build();
            return fileTestRepository.saveAndFlush(fileTestResult);
        }
        fileTest.setFile(fileResult);
        return fileTestRepository.save(fileTest);
    }
}

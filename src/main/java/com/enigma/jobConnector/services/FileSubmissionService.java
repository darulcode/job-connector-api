package com.enigma.jobConnector.services;

import com.enigma.jobConnector.entity.FileSubmissionTest;
import com.enigma.jobConnector.entity.TestDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileSubmissionService {
    FileSubmissionTest createFileSubmission(MultipartFile file, TestDetail testDetail) throws IOException;
}

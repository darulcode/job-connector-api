package com.enigma.jobConnector.services;

import com.enigma.jobConnector.entity.FileTest;
import com.enigma.jobConnector.entity.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileTestService {
    FileTest createFile(MultipartFile file) throws IOException;
    FileTest udpdateFileTest(Test test, MultipartFile file) throws IOException;

}

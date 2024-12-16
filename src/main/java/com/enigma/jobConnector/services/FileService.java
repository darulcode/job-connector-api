package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.response.FileResponse;
import com.enigma.jobConnector.dto.response.GetFileResponse;
import com.enigma.jobConnector.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileResponse uploadFile(MultipartFile file) throws IOException;

    FileResponse getFileById(String id);
    GetFileResponse getFileFromCloudinary(String id) throws IOException;
    File getOne(String id);
    void deleteFile(String id);
}

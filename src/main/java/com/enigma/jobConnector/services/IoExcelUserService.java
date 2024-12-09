package com.enigma.jobConnector.services;

import com.enigma.jobConnector.dto.response.ImportUserResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IoExcelUserService {
    ImportUserResponse importExcelUserData(MultipartFile file);
    void exportExcelUserData(HttpServletResponse response);
}

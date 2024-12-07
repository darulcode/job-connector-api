package com.enigma.jobConnector.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IoExcelUserService {
    void importExcelUserData(MultipartFile file);
    void exportExcelUserData(HttpServletResponse response);
}

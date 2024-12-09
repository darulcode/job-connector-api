package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.response.ImportUserResponse;
import com.enigma.jobConnector.services.IoExcelUserService;
import com.enigma.jobConnector.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constant.USER_API + "/data")
@RequiredArgsConstructor
public class IoExcelUserController {
    private final IoExcelUserService ioExcelUserService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<?> importUser(@RequestParam MultipartFile file) {
        ImportUserResponse response = ioExcelUserService.importExcelUserData(file);
        HttpStatus httpStatus = response.getFailedImportCount() > 0 ? HttpStatus.CREATED : HttpStatus.CONFLICT;
        return ResponseUtil.buildResponse(httpStatus, String.format(
                Constant.SUCCESS_IMPORT_USER, response.getSuccessImportCount(), response.getFailedImportCount()
        ), response);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/export")
    public void exportUser(HttpServletResponse response) {
        ioExcelUserService.exportExcelUserData(response);
    }
}

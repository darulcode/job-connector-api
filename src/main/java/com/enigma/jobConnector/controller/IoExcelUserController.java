package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
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
        ioExcelUserService.importExcelUserData(file);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_IMPORT_USER, null);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/export")
    public void exportUser(HttpServletResponse response) {
        ioExcelUserService.exportExcelUserData(response);
    }
}

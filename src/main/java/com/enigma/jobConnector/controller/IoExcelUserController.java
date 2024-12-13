package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.response.ImportUserResponse;
import com.enigma.jobConnector.services.IoExcelUserService;
import com.enigma.jobConnector.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "I/O excel user", description = "APIs for create or export user using excel file")
public class IoExcelUserController {
    private final IoExcelUserService ioExcelUserService;

    @Operation(summary = "create users with excel file")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<?> importUser(@RequestParam MultipartFile file) {
        ImportUserResponse response = ioExcelUserService.importExcelUserData(file);
        HttpStatus httpStatus = response.getMessage().isEmpty() ? HttpStatus.CREATED : HttpStatus.CONFLICT;
        String message = response.getMessage().isEmpty() ? Constant.SUCCESS_IMPORT_USER : Constant.FAILED_IMPORT_USER + response.getMessage();
        return ResponseUtil.buildResponse(httpStatus, message, null);
    }

    @Operation(summary = "export user output excel file")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/export")
    public void exportUser(HttpServletResponse response) {
        ioExcelUserService.exportExcelUserData(response);
    }
}

package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.response.GetFileResponse;
import com.enigma.jobConnector.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping(Constant.FILE_API)
@RequiredArgsConstructor
@Tag(name = "File", description = "APIs for get file from database")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "get file")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) throws IOException {
        GetFileResponse response = fileService.getFileFromCloudinary(id);
        HttpHeaders headers = new HttpHeaders();

        String mediaType = response.getMediaType();
        String fileName = response.getFileName();

        String subtype = mediaType.split("/")[1];
        if (subtype.equalsIgnoreCase("pdf")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getFile());
        } else if (!subtype.equalsIgnoreCase("image")) {
            headers.setContentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("attachment", fileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getFile());
        } else {
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.valueOf(mediaType))
                    .body(response.getFile());
        }
    }

}

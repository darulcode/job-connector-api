package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.dto.response.GetFileResponse;
import com.enigma.jobConnector.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(Constant.FILE_API)
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) throws IOException {
        GetFileResponse response = fileService.getFileFromCloudinary(id);
        HttpHeaders headers = new HttpHeaders();
        if (!response.getMediaType().split("/")[1].equals("image")) {
            headers.setContentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("attachment", response.getFileName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getFile());
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(response.getMediaType()))
                    .body(response);
        }
    }
}

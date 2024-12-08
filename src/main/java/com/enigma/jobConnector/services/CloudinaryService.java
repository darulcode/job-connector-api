package com.enigma.jobConnector.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map<String, Object> uploadImage(MultipartFile file);
    Map<String, Object> uploadFile(MultipartFile file);
    String deleteFile(String publicId);
    byte[] getFile(String imagePath);
}
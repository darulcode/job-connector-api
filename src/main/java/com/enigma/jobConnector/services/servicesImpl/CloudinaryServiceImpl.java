package com.enigma.jobConnector.services.servicesImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.enigma.jobConnector.client.CloudinaryFeignClient;
import com.enigma.jobConnector.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private final CloudinaryFeignClient cloudinaryFeignClient;


    @Override
    public Map<String, Object> uploadImage(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public Map<String, Object> uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);

            String randomName = UUID.randomUUID().toString();

            String filenameWithExtension = randomName + "." + extension;
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto", "public_id", filenameWithExtension));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public String deleteFile(String publicId) {
        try {
            Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return (String) deleteResult.get("result");
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public byte[] getFile(String imagePath) {
        try {
            return cloudinaryFeignClient.getImage(imagePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch file", e);
        }
    }
}

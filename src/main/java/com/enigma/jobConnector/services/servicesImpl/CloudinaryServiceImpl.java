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

    /**
     * Upload an image to Cloudinary.
     *
     * @param file MultipartFile representing the image to be uploaded.
     * @return Map containing the upload result from Cloudinary.
     */
    @Override
    public Map<String, Object> uploadImage(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );
        } catch (IOException e) {
            log.error("Error while uploading image to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to upload image. Please try again.", e);
        }
    }

    /**
     * Upload any file (image, pdf, etc.) to Cloudinary.
     *
     * @param file MultipartFile representing the file to be uploaded.
     * @return Map containing the upload result from Cloudinary.
     */
    @Override
    public Map<String, Object> uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new RuntimeException("File must have a valid name.");
            }

            String baseName = FilenameUtils.getBaseName(originalFilename);
            String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();

            if (extension.isBlank()) {
                throw new RuntimeException("File must have a valid extension.");
            }

            String uniqueFileName = UUID.randomUUID().toString();

            String resourceType = extension.equals("pdf") || extension.equals("docx") ? "raw" : "auto";

            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "public_id", uniqueFileName
                    )
            );
        } catch (IOException e) {
            log.error("Error while uploading file to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file. Please try again.", e);
        }
    }


    /**
     * Delete a file from Cloudinary by its public ID.
     *
     * @param publicId Public ID of the file to be deleted.
     * @return Result of the deletion operation.
     */
    @Override
    public String deleteFile(String publicId) {
        try {
            Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return (String) deleteResult.get("result");
        } catch (IOException e) {
            log.error("Error while deleting file from Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to delete file. Please try again.", e);
        }
    }

    /**
     * Fetch a file from Cloudinary using its path.
     *
     * @param imagePath Path to the file in Cloudinary.
     * @return Byte array representing the file content.
     */
    @Override
    public byte[] getFile(String imagePath) {
        try {
            return cloudinaryFeignClient.getImage(imagePath);
        } catch (Exception e) {
            log.error("Error while fetching file from Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch file. Please try again.", e);
        }
    }
}

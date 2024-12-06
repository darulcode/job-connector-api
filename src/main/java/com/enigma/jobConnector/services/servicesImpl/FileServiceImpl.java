package com.enigma.jobConnector.services.servicesImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.enigma.jobConnector.client.CloudinaryClient;
import com.enigma.jobConnector.dto.response.FileResponse;
import com.enigma.jobConnector.dto.response.GetFileResponse;
import com.enigma.jobConnector.entity.File;
import com.enigma.jobConnector.repository.FileRepository;
import com.enigma.jobConnector.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final CloudinaryClient cloudinaryClient;
    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloudName}")
    private String cloudinaryCloudName;

    @Override
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        String folder = "jobConnector";

        Map uploadResult;
        String mediaType = "unknown";
        String format = "unknown";
        String publicId = "";

        if (file.getContentType().startsWith("image")) {
            ResponseEntity<Map> uploadResponse = cloudinaryClient.uploadImage(cloudinaryCloudName, file, folder);
            uploadResult = uploadResponse.getBody();
            mediaType = "image";
        } else {
            ResponseEntity<Map> uploadResponse = cloudinaryClient.uploadRawFile(cloudinaryCloudName, file, folder);
            uploadResult = uploadResponse.getBody();
            mediaType = "raw";
        }
        if (uploadResult != null) {
            publicId = (String) uploadResult.get("public_id");
            format = (String) uploadResult.get("format");
        }
        File fileResult = File.builder()
                .name(file.getOriginalFilename())
                .publicId(publicId)
                .mediaType(mediaType + "/" + format)
                .urlPath((String) uploadResult.get("secure_url"))
                .build();
        fileRepository.saveAndFlush(fileResult);
        return getFileResponse(fileResult);
    }

    @Override
    public FileResponse getFileById(String id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        return getFileResponse(file);
    }

    @Override
    public GetFileResponse getFileFromCloudinary(String publicId) throws IOException {
        File file = getOne(publicId);
        ResponseEntity<byte[]> responseEntity;
        GetFileResponse fileResponse = null;

        try {
            if (file.getMediaType().startsWith("image")) {
                responseEntity = cloudinaryClient.getImage(cloudinaryCloudName, file.getPublicId());
            } else {
                responseEntity = cloudinaryClient.getRawFile(cloudinaryCloudName, file.getPublicId());
            }
            byte[] fileData = responseEntity.getBody();
            fileResponse = GetFileResponse.builder()
                    .file(fileData)
                    .mediaType(file.getMediaType())
                    .fileName(file.getName())
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return fileResponse;
    }

    @Override
    public File getOne(String id) {
        return fileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"File not found"));
    }

    @Override
    public void deleteFile(String id) {
        File file = getOne(id);
        try {
            cloudinary.uploader().destroy(file.getPublicId(), ObjectUtils.emptyMap());
            fileRepository.delete(file);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private FileResponse getFileResponse(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .fileName(file.getName())
                .mediaType(file.getMediaType())
                .url(file.getUrlPath())
                .build();
    }
}

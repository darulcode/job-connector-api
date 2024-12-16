package com.enigma.jobConnector.services.servicesImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.enigma.jobConnector.dto.response.FileResponse;
import com.enigma.jobConnector.dto.response.GetFileResponse;
import com.enigma.jobConnector.entity.File;
import com.enigma.jobConnector.repository.FileRepository;
import com.enigma.jobConnector.services.CloudinaryService;
import com.enigma.jobConnector.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final Cloudinary cloudinary;
    private final CloudinaryService cloudinaryService;

    @Value("${cloudinary.name}")
    private String cloudinaryCloudName;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileResponse uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must not be empty");
        }
        String mediaType;
        String fileExtension = "";
        Map<String, Object> uploadResponse;

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }

        if (file.getContentType() != null && file.getContentType().startsWith("image")) {
            uploadResponse = cloudinaryService.uploadImage(file);
            mediaType = "image";
        } else {
            uploadResponse = cloudinaryService.uploadFile(file);
            mediaType = "raw";
        }

        String publicId = (String) uploadResponse.get("public_id");
        String secureUrl = (String) uploadResponse.get("secure_url");

        File fileResult = File.builder()
                .name(originalFilename)
                .publicId(publicId)
                .mediaType(mediaType + "/" + fileExtension)
                .urlPath(secureUrl)
                .build();

        fileRepository.save(fileResult);
        return getFileResponse(fileResult);
    }


    @Override
    public FileResponse getFileById(String id) {
        File file = getOne(id);
        return getFileResponse(file);
    }


    @Override
    public GetFileResponse getFileFromCloudinary(String id) throws IOException {
        File file = getOne(id);

        if (file == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        byte[] responseEntity;

        if (file.getMediaType().startsWith("image")) {
            responseEntity = cloudinaryService.getFile("image/upload"+file.getUrlPath().split("upload")[1]);
        } else {
            responseEntity = cloudinaryService.getFile("raw/upload" + file.getUrlPath().split("upload")[1]);
        }


        return GetFileResponse.builder()
                .file(responseEntity)
                .mediaType(file.getMediaType())
                .fileName(file.getName())
                .build();
    }

    @Override
    public File getOne(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
    }

    @Override
    public void deleteFile(String id) {
        File file = getOne(id);

        try {
            cloudinary.uploader().destroy(file.getPublicId(), ObjectUtils.emptyMap());
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file: " + e.getMessage());
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

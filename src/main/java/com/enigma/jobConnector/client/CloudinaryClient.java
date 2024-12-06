package com.enigma.jobConnector.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@FeignClient(name = "cloudinary", url = "https://api.cloudinary.com")
public interface CloudinaryClient {

    @PostMapping("/v1_1/{cloudName}/image/upload")
    ResponseEntity<Map> uploadImage(@PathVariable("cloudName") String cloudName,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("folder") String folder);

    @PostMapping("/v1_1/{cloudName}/raw/upload")
    ResponseEntity<Map> uploadRawFile(@PathVariable("cloudName") String cloudName,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("folder") String folder);

    @GetMapping("/v1_1/{cloudName}/image/upload/{publicId}")
    ResponseEntity<byte[]> getImage(@PathVariable("cloudName") String cloudName,
                                    @PathVariable("publicId") String publicId);

    @GetMapping("/v1_1/{cloudName}/raw/upload/{publicId}")
    ResponseEntity<byte[]> getRawFile(@PathVariable("cloudName") String cloudName,
                                      @PathVariable("publicId") String publicId);
}

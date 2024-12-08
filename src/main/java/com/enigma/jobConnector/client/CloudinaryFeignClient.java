package com.enigma.jobConnector.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@FeignClient(name = "cloudinary", url = "https://res.cloudinary.com/dgodtvrvm/")
public interface CloudinaryFeignClient {

    @GetMapping("/{imagePath}")
    byte[] getImage(@PathVariable("imagePath") String imagePath);
}
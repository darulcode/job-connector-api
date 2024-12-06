package com.enigma.jobConnector.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    @Value("${cloudinary.name}")
    private String CLOUDINARY_NAME;
    @Value("${cloudinary.api_key}")
    private String CLOUDINARY_API_KEY;
    @Value("${cloudinary.secret_ky}")
    private String CLOUDINARY_SECRET_KEY;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", CLOUDINARY_NAME,
                "api_key", CLOUDINARY_API_KEY,
                "api_secret", CLOUDINARY_SECRET_KEY
        );
        return new Cloudinary(config);
    }
}


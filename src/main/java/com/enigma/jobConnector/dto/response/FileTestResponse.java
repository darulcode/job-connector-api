package com.enigma.jobConnector.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileTestResponse {
    private String fileName;
    private String urlFile;
}

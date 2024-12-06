package com.enigma.jobConnector.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFileResponse {
    private byte[] file;
    private String mediaType;
    private String fileName;
}

package com.enigma.jobConnector.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZipFileResponse {
    private byte[] zipContent;
    private String fileName;
}

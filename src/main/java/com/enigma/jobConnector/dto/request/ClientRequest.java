package com.enigma.jobConnector.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRequest {
    private String id;
    private String name;
}

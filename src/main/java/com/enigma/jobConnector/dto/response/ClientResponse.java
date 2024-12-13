package com.enigma.jobConnector.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {
    private String id;
    private String name;
    private String address;
}

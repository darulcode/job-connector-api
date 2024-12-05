package com.enigma.jobConnector.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String username;
    private String password;
    private String email;
    private String role;
}

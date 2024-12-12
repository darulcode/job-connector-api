package com.enigma.jobConnector.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String phoneNumber;
    private String password;
    private String email;
    private String categoryId;
    private String role;
}

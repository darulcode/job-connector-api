package com.enigma.jobConnector.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private UserCategoryResponse category;
}

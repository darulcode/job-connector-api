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
    private String username;
    private String role;
    private UserCategoryResponse category;
}

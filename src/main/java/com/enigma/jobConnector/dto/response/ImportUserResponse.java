package com.enigma.jobConnector.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportUserResponse {
    Integer successImportCount;
    Integer failedImportCount;
    List<UserResponse> successImportedUser;
    List<FailedImportUserResponse> failedImportedUser;
}

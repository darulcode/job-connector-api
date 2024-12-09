package com.enigma.jobConnector.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusSubmissionRequest {
    private String status;
}

package com.enigma.jobConnector.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeStatusTestRequest {
    private String status;
}

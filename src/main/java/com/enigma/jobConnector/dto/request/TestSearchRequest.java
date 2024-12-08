package com.enigma.jobConnector.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TestSearchRequest extends PagingAndShortingRequest{
    private String query;
    private String status;
    private String admin;
    private String client;
}

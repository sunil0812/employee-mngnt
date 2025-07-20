package com.employee.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpRequest {
    @JsonProperty("integrated_number")
    private String integratedNumber;
    @JsonProperty("content_type")
    private String contentType;
    private Map<String,Object> payload;



}


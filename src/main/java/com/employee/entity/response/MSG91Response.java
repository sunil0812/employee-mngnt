package com.employee.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MSG91Response {
    private String status;
    private String hasError;
    private String data;
    private List<String> errors;
}

package com.employee.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateResponse {

    private String status;
    private String message;

}

package com.employee.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEmployeeResponse {
    private String message;
    private EmployeeResponse employeeResponse;
    private ManagerResponse managerResponse;
}

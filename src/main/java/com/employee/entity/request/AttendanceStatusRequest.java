package com.employee.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttendanceStatusRequest {
    @NotNull(message = "Employee Id should not be null")
    private String employeeId;
    @NotNull(message = "Status should not be null")
        private String status;
}

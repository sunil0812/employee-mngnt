package com.employee.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LogInRequest {
    @NotNull(message = "user name should not be empty")
    private String userName;
    @NotNull(message = "password should not be empty")
    private String password;
}

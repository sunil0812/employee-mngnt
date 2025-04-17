package com.employee.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

    @NotNull(message = "Field should not be empty")
    private String newPassword;
    @NotNull(message = "Field should not be empty")
    private String confirmPassword;
}

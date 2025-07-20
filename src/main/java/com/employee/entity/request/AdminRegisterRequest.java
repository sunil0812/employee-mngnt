package com.employee.entity.request;

import com.employee.model.Address;
import com.employee.model.BankDetails;
import com.employee.model.Company;
import com.employee.model.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {
    @Schema(description = "Name of the employee", example = "John Doe")
    @NotNull
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Invalid name format")
    private String name;

    @Schema(description = "Email address of the employee", example = "johndoe@example.com")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Size(min = 1, max = 256)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "email doesn't match exactly")
    private String email;

    @Schema(description = "Phone number of the employee", example = "+1234567890")
    @NotNull
    @Pattern(regexp = "^\\+\\d{1,3}[- ]?\\d{10}$", message = "phone number doesn't match exactly")
    private String phone;

    @Schema(description = "Gender of the employee", example = "Male")
    @NotNull
    private String gender;

    @NotNull
    @Schema(description = "Date of birth of the employee", example = "1990-01-01")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Date Of Birth should be match the regex")
    private String dob;

    @Schema(description = "Address details of the employee")
    @NotNull
    private Address address;

    @Schema(description = "Bank details of the employee")
    @NotNull
    private BankDetails bankDetails;

    @Schema(description = "Company details of the employee")
    @NotNull
    private Company companyDetails;

}

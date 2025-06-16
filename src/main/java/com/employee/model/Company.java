package com.employee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @JsonProperty
    @NotNull(message = "Company name should not be null")
    private String name;
    @JsonProperty
    @NotNull(message = "email No should not be null")
    private String email;
    @JsonProperty
    @NotNull(message = "Phone No should not be null")
    private String phone;
    @JsonProperty
    @NotNull(message = "Company Address should not be null")
    private Address address;
}

package com.employee.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateDetails {

    @NotNull
    private String field;

    @NotNull
    private String value;

    public static String PHONE="PHONE: ";
    public static String NAME="NAME: ";
    public static String EMAIL="EMAIL: ";
    public static String COMPNAME="COMPANY_NAME: ";
}

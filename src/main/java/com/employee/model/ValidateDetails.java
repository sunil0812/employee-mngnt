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

    public static final String PHONE="ADMIN_PHONE: ";
    public static final String NAME="ADMIN_NAME: ";
    public static final String EMAIL="ADMIN_EMAIL: ";
    public static final String COMPNAME="COMPANY_NAME: ";
    public static final String COMPDOMAIN="COMPANY_DOMAIN: ";
    public static final String COMPPHONE="COMPANY_PHONE: ";
}

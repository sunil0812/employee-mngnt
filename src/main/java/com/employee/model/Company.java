package com.employee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String name;
    @JsonProperty
    private String phoneNo;
    @JsonProperty
    private Address companyAddress;
}

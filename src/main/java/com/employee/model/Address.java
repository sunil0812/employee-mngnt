package com.employee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class Address {
    @JsonProperty
    @Schema(description = "Street no of the employee", example = "123")
    private String no;
    @JsonProperty
    @Schema(description = "Street address of the employee", example = " Main St")
    private String street;
    @JsonProperty
    @Schema(description = "Pin code of the employee", example = "10001")
    private String pinCode;
    @JsonProperty
    @Schema(description = "Country of the employee",example = "USA")
    private String country;

    public Address(String no,String street,String pinCode,String country){
        this.no = no;
        this.street = street;
        this.pinCode = pinCode;
        this.country = country;
    }
}

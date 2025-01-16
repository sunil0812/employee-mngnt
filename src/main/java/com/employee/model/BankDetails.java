package com.employee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class BankDetails {
    @JsonProperty
    @Schema(description = "Account number of the employee", example = "1234567890")
    @NotNull(message = "Account number should not be null")
    @Size(min = 1, max = 255)
    private String accountNo;
    @JsonProperty
    @Schema(description = "IFSC code of the bank", example = "IFSC1234")
    @NotNull(message = "IFSC code should not be null")
    @Size(min = 1, max = 255)
    private String ifscCode;

    public BankDetails(String accountNo,String ifscCode){
        this.accountNo = accountNo;
        this.ifscCode = ifscCode;
    }
}

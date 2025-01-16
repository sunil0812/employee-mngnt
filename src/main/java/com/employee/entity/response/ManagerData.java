package com.employee.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ManagerData {
    @JsonProperty
    private String name;
    @JsonProperty
    private String empId;

    public ManagerData(String name, String empId) {
        this.name = name;
        this.empId = empId;
    }
}

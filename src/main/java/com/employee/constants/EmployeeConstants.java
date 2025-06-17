package com.employee.constants;

import com.employee.exception.EmployeeExceptions;
import lombok.Data;

import java.util.List;

public enum EmployeeConstants {

    SOFTWARE_ENGINEER(List.of("software_engineer", "developer")),
    ADMIN(List.of("admin")),
    HR(List.of("human_resources")),
    MANAGER(List.of("manager")),
    TESTER(List.of("quality_analysis")),
    ANALYST(List.of("business_analyst")),
    SENIOR_DEVELOPER(List.of("senior_engineer")),
    SENIOR_ANALYST(List.of("senior_BA")),
    SENIOR_TESTER(List.of("quality_analysis"));

    public static final List<String> STATUS = List.of("remove", "delete");

    private List<String> values;

    public List<String> getValues() {
        return this.values;
    }

    EmployeeConstants(List<String> values) {
        this.values = values;
    }

    public static EmployeeConstants validateRole(String role) {
        for (EmployeeConstants constant : EmployeeConstants.values()) {
            if (constant.getValues().contains(role.toLowerCase())) {
                return constant;
            }
        }
        throw new EmployeeExceptions("Given Role Not found :" + role);
    }
}

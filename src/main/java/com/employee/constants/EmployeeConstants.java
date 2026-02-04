package com.employee.constants;

import com.employee.exception.EmployeeExceptions;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static String validateRole(String role) {

        String empRole = Arrays.stream(EmployeeConstants.values())
                .filter(e -> e.getValues().stream().anyMatch(r -> r.equalsIgnoreCase(role)))
                .map(EmployeeConstants::name).collect(Collectors.joining());
        if (empRole.isBlank()) {
            throw new EmployeeExceptions("Given Role Not found :" + role);
        }
        return empRole;
    }
}

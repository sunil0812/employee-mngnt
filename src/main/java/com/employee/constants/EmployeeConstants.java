package com.employee.constants;

import lombok.Data;

import java.util.List;

public enum EmployeeConstants {

    SOFTWARE_ENGINEER(List.of("software_engineer","developer","engineer")),
    MANAGER(List.of("manager","manage")),
    TESTER(List.of("")),
    ANALYST(List.of("")),
    SENIOR_DEVELOPER(List.of("")),
    SENIOR_ANALYST(List.of("")),
    SENIOR_TESTER(List.of(""));

    public static final List<String> STATUS = List.of("remove","delete");

    private List<String> values;

    public List<String> getValues() {
        return this.values;
    }
    EmployeeConstants(List<String> values) {
        this.values = values;
    }


}

package com.employee.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EmployeeResponseDto {

    private String name;
    private String empId;
    private String role;
    private String email;
    private String gender;
    private String phone;
    private String dob;
    private String teamName;
    private String address;
    private List<String> teamMembers;
    private String bankDetails;
    private String managerEmpId;
    private String managerName;


    public EmployeeResponseDto(String name, String empId, String role, String email, String gender, String phone, String teamName, String address, String bankDetails, String managerEmpId, String managerName) {
        this.name = name;
        this.empId = empId;
        this.role = role;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.teamName = teamName;
        this.address = address;
        this.bankDetails = bankDetails;
        this.managerEmpId = managerEmpId;
        this.managerName = managerName;
    }

    public EmployeeResponseDto(String address) {
        this.address = address;
    }


    public EmployeeResponseDto(String name, String empId, String role, String email, String gender, String phone, String teamName, String address, String bankDetails, List<String> teamMembers) {
        this.name = name;
        this.empId = empId;
        this.role = role;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.teamName = teamName;
        this.address = address;
        this.bankDetails = bankDetails;
        this.teamMembers = teamMembers;
    }

}


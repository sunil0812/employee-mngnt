package com.employee.entity.response;

import com.employee.model.Address;
import com.employee.model.BankDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeResponse {
    private String name;
    private String empId;
    private String role;
    private String email;
    private String gender;
    private String phone;
    private String teamName;
    private Address address;
    private BankDetails bankDetails;
    private ManagerData manager;


}

package com.employee.service;

import com.employee.entity.request.AdminRegisterRequest;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.AdminRegister;
import com.employee.repository.AdminRegisterRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminRegisterService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AdminRegisterRepo repo;

    public String register(AdminRegisterRequest adminRegister) {
        try {
            AdminRegister value = AdminRegister.builder().name(adminRegister.getName())
                    .gender(adminRegister.getGender())
                    .email(adminRegister.getEmail())
                    .phone(adminRegister.getPhone())
                    .dob(adminRegister.getDob())
                    .address(mapper.writeValueAsString(adminRegister.getAddress()))
                    .bankDetails(mapper.writeValueAsString(adminRegister.getBankDetails()))
                    .companyDetails(mapper.writeValueAsString(adminRegister.getCompanyDetails()))
                    .teamDetails(adminRegister.getTeamName()).build();
            repo.save(value);
        } catch (JsonProcessingException exception) {
            throw new EmployeeExceptions(exception.getMessage());
        }
        return "Admin Detail Registered";
    }
}

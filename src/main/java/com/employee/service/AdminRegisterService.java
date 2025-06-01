package com.employee.service;

import com.employee.entity.request.AdminRegisterRequest;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.AdminRegister;
import com.employee.model.ValidateDetails;
import com.employee.repository.AdminRegisterRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.employee.model.ValidateDetails.COMPNAME;
import static com.employee.model.ValidateDetails.EMAIL;
import static com.employee.model.ValidateDetails.NAME;
import static com.employee.model.ValidateDetails.PHONE;


@Service
public class AdminRegisterService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AdminRegisterRepo repo;

    public String register(AdminRegisterRequest adminRegister) {
        try {
            AdminRegister value = AdminRegister.builder()
                    .name(adminRegister.getName().toLowerCase())
                    .gender(adminRegister.getGender().toLowerCase())
                    .email(adminRegister.getEmail().toLowerCase())
                    .phone(adminRegister.getPhone())
                    .dob(adminRegister.getDob())
                    .address(mapper.writeValueAsString(adminRegister.getAddress()))
                    .bankDetails(mapper.writeValueAsString(adminRegister.getBankDetails()))
                    .companyDetails(mapper.writeValueAsString(adminRegister.getCompanyDetails()))
                    .build();
            repo.save(value);
        } catch (JsonProcessingException exception) {
            throw new EmployeeExceptions(exception.getMessage());
        }
        return "Admin Detail Registered";
    }

    public String validate(ValidateDetails details) {
        String validated = "Validated ";
        return switch (details.getField()) {
            case "phone" -> {
                if (repo.existsByPhone(details.getValue())) {
                    throw new EmployeeExceptions("Phone Number Already Taken");
                }
                yield PHONE + validated;
            }
            case "email" -> {
                boolean matched = isValidEmail(details.getValue());
                if (repo.existsByEmail(details.getValue()) || !matched) {
                    throw new EmployeeExceptions(matched ? "Email Already Taken" : "InValid Email" );
                }
                yield EMAIL + validated;
            }
            case "name" -> {
                if (repo.existsByName(details.getValue())) {
                    throw new EmployeeExceptions("Name Already Taken");
                }
                yield NAME + validated;
            }
            case "companyName" -> {
                if (repo.existsByCompanyName(details.getValue())) {
                    throw new EmployeeExceptions("Company Name Already Taken");
                }
                yield  COMPNAME + validated;
            }
            default -> "Given Field not for validation";
        };
    }

    private boolean isValidEmail(String mail) {
       return Pattern.compile("[a-zA-Z0-9]+@[a-z]{3,10}.[a-z]{3}").matcher(mail).matches();
    }
}
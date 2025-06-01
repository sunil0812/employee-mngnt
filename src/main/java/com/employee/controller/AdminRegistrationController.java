package com.employee.controller;

import com.employee.entity.request.AdminRegisterRequest;
import com.employee.model.AdminRegister;
import com.employee.model.ValidateDetails;
import com.employee.service.AdminRegisterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class AdminRegistrationController {
    @Autowired
    private AdminRegisterService adminService;

    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody AdminRegisterRequest adminRegister) {
       return ResponseEntity.ok(adminService.register(adminRegister));
    }

    @PutMapping("/validate-details")
    public ResponseEntity<String> validateAdminDetails(@Valid @RequestBody ValidateDetails details){
        return ResponseEntity.ok(adminService.validate(details));
    }

}

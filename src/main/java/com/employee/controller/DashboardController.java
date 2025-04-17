package com.employee.controller;

import com.employee.entity.request.LogInRequest;
import com.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private EmployeeService service;

    @PutMapping("/logIn")
    public ResponseEntity<String> dashboardLogIn(@Validated @RequestBody LogInRequest request){
       return ResponseEntity.ok(service.validateCredentials(request));
    }
}

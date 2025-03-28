package com.employee.controller;


import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.request.LogInRequest;
import com.employee.entity.response.BaseEmployeeResponse;
import com.employee.entity.response.UpdateResponse;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;

    }


    @PostMapping("/save")
    public ResponseEntity<Employee> saveEmployee(@Validated @RequestBody EmployeeEntity emp) {
        Employee employee = service.saveEmp(emp);
        return ResponseEntity.ok(employee);
    }

    //    Get Employee data
    @GetMapping("/getEmployee/{empId}")
    public ResponseEntity<BaseEmployeeResponse> getEmployee(@PathVariable String empId) {
        BaseEmployeeResponse response = service.getEmpResponse(empId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getEmployeeList/{empId}")
    public ResponseEntity<List<Employee>> getAllEmployee(@PathVariable String empId) {
        List<Employee> response = service.getAllEmpResponse(empId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{empId}")
    public ResponseEntity<UpdateResponse> updateEmployee(@PathVariable String empId, @RequestBody EmployeeEntity currentValue) {

        return ResponseEntity.ok(service.updateDetails(empId, currentValue));
    }

    @PutMapping("/update/{empId}/{status}")
    public ResponseEntity<String> updateEmployeeStatus(@PathVariable String empId, @PathVariable String status) {

        return ResponseEntity.ok(service.updateStatus(empId, status.toLowerCase()));
    }

    @PutMapping("/login")
    public ResponseEntity<String> loginEmp(@Validated @RequestBody LogInRequest request){
        return ResponseEntity.ok(service.validateCredentials(request));
    }


}

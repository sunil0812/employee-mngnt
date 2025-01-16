package com.employee.controller;


import com.employee.configuration.StatusConfiguration;
import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.response.BaseEmployeeResponse;
import com.employee.entity.response.UpdateResponse;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getEmployee")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        List<Employee> response = service.getAllEmpResponse();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{empId}")
    public ResponseEntity<UpdateResponse> updateEmployee(@PathVariable String empId, @RequestBody EmployeeEntity currentValue, @PathVariable String status) {

        return ResponseEntity.ok(service.updateDetails(empId, currentValue));
    }
    @PutMapping("/update/{empId}/{status}")
    public ResponseEntity<String> updateEmployeeStatus(@PathVariable String empId, @PathVariable String status) {

        return ResponseEntity.ok(service.updateStatus(empId, status.toLowerCase()));
    }

}

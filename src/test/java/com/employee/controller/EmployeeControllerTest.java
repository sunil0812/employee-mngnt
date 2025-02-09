package com.employee.controller;

import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.response.BaseEmployeeResponse;
import com.employee.entity.response.EmployeeResponse;
import com.employee.entity.response.UpdateResponse;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController controller;

    @Mock
    private EmployeeService service;

    @Test
    void shall_test_save_emp() {

        EmployeeEntity request = EmployeeEntity.builder().name("jhon").role("software_engineer").email("jhon123@gmail.com").gender("M").empType("F").phone("1234567890").build();
        Employee response = new Employee();
        response.setActive(true);
        Mockito.when(service.saveEmp(request)).thenReturn(response);
        ResponseEntity<Employee> empResponse = controller.saveEmployee(request);
        Assertions.assertEquals(HttpStatus.OK, empResponse.getStatusCode());

    }

    @Test
    void shall_test_save_emp_exception() {

        EmployeeEntity request = EmployeeEntity.builder().name("jhon").role("software_engineer").email("jhon123@gmail.com").gender("M").empType("F").phone("1234567890").build();
        Employee response = new Employee();
        response.setActive(true);
        Mockito.when(service.saveEmp(request)).thenThrow(new EmployeeExceptions("No team found "));
        EmployeeExceptions empResponse = assertThrows(EmployeeExceptions.class, () -> controller.saveEmployee(request));
        Assertions.assertEquals("No team found ", empResponse.getMessage());

    }

    @Test
    void shall_test_get_emp() {
        BaseEmployeeResponse response = BaseEmployeeResponse.builder().employeeResponse(EmployeeResponse.builder().name("we").build()).build();

        Mockito.when(service.getEmpResponse("we123")).thenReturn(response);
        ResponseEntity<BaseEmployeeResponse> empResponse = controller.getEmployee("we123");
        Assertions.assertEquals(HttpStatus.OK, empResponse.getStatusCode());
        Assertions.assertEquals(response.getEmployeeResponse(), Objects.requireNonNull(empResponse.getBody()).getEmployeeResponse());
        Assertions.assertNull(response.getManagerResponse());

    }

    @Test
    void shall_test_get_all_emp() {
        Employee response = new Employee();
        response.setActive(true);
        response.setName("we13");
        response.setEmpId("we123");

        Mockito.when(service.getAllEmpResponse()).thenReturn(List.of(response));
        ResponseEntity<List<Employee>> empResponse = controller.getAllEmployee();
        Assertions.assertEquals(HttpStatus.OK, empResponse.getStatusCode());
        Assertions.assertEquals(Objects.requireNonNull(empResponse.getBody()).get(0).getName(), response.getName());
    }

    @Test
    void shall_test_update_emp() {
        EmployeeEntity request = EmployeeEntity.builder().name("jhon").role("software_engineer").email("jhon123@gmail.com").gender("M").empType("F").phone("1234567890").build();
        UpdateResponse response = UpdateResponse.builder().status("updated").message("employee updated successfully").build();
        Mockito.when(service.updateDetails("we123", request)).thenReturn(response);
        ResponseEntity<UpdateResponse> empResponse = controller.updateEmployee("we123", request);
        Assertions.assertEquals(HttpStatus.OK, empResponse.getStatusCode());
        Assertions.assertEquals(response.getMessage(), Objects.requireNonNull(empResponse.getBody()).getMessage());
    }

    @Test
    void shall_test_update_emp_status() {

        Mockito.when(service.updateStatus("we123", "remove")).thenReturn("removed");
        ResponseEntity<String> empResponse = controller.updateEmployeeStatus("we123", "remove");
        Assertions.assertEquals(HttpStatus.OK, empResponse.getStatusCode());
        Assertions.assertEquals("removed", Objects.requireNonNull(empResponse.getBody()));
    }
}

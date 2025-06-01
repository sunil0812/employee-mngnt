package com.employee.service;

import com.employee.entity.dto.TeamResponseDto;
import com.employee.entity.response.TeamData;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Team;
import com.employee.model.ValidateDetails;
import com.employee.repository.AdminRegisterRepo;
import com.employee.repository.TeamRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminRegisterServiceTest {

    @InjectMocks
    private AdminRegisterService service;

    @Mock
    private AdminRegisterRepo repo;


    @Test
    void testValidateDetailsOfAdmin(){
        String phone = "9878574723";
        ValidateDetails validate = ValidateDetails.builder().field("phone").value(phone).build();

        when(repo.existsByPhone(phone)).thenReturn(false);

        String response = service.validate(validate);

        assertThat(response).contains("Validated");
    }

    @Test
    void testValidateDetailsOfAdminEmail(){
        String email = "name123@gmail.com";
        ValidateDetails validate = ValidateDetails.builder().field("email").value(email).build();

        when(repo.existsByEmail(email)).thenReturn(false);

        String response = service.validate(validate);

        assertThat(response).contains("Validated");
    }


    @Test
    void testValidateDetailsOfAdminInValidEmail(){
        String email = "name-123@gmail.com";
        ValidateDetails validate = ValidateDetails.builder().field("email").value(email).build();

        when(repo.existsByEmail(email)).thenReturn(false);

        Exception response = assertThrows(EmployeeExceptions.class,() -> service.validate(validate));

        assertThat(response.getMessage()).contains("InValid Email");
    }

    @Test
    void testValidateDetailsOfAdminEmailAlreadyTaken(){
        String email = "name123@gmail.com";
        ValidateDetails validate = ValidateDetails.builder().field("email").value(email).build();

        when(repo.existsByEmail(email)).thenReturn(true);

        Exception response = assertThrows(EmployeeExceptions.class,() -> service.validate(validate));

        assertThat(response.getMessage()).contains("Already Taken");
    }

}

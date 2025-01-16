package com.employee.service;

import com.employee.configuration.StatusConfiguration;
import com.employee.entity.dto.EmployeeResponseDto;

import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.response.BaseEmployeeResponse;

import com.employee.entity.response.UpdateResponse;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Address;
import com.employee.model.BankDetails;
import com.employee.model.Employee;
import com.employee.model.Manager;
import com.employee.model.Team;
import com.employee.repository.EmployeeNameTrackingRepo;
import com.employee.repository.EmployeeRepo;
import com.employee.repository.ManagerRepo;
import com.employee.repository.TeamRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService service;

    @Mock
    private ManagerRepo managerRepo;

    @Mock
    private TeamRepo teamRepo;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private EmployeeRepo empRepo;

    @Mock
    private EmployeeNameTrackingRepo nameTrackingRepo;

    @Mock
    private StatusConfiguration statusConfiguration;

    @Test
    void given_employee_data_when_developer_then_save() throws Exception {
        //        given
        EmployeeEntity req = EmployeeEntity.builder().name("name N").email("name@gmail.com").teamName("team1").gender("M").empType("F").role("software_engineer").managerEmpId("MG12").build();

        Manager manager = new Manager();
        manager.setTeamName("team1");

        Team team = new Team();
        team.setName("team1");
        team.setTeamMembers(new ArrayList<>());
        team.setTeamCount(0);

        Employee emp = new Employee();
        emp.setEmpId("NEF109");
        emp.setEmpType(req.getEmpType());
        emp.setEmail(req.getEmail());
        emp.setGender(req.getGender());
        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setRole(req.getRole());

        String[] listName = req.getName().split(" ");
        String firstName = listName[0].length() >= 2 ? listName[0] : listName[1];

        //        when
        when(nameTrackingRepo.findByFirstName(firstName)).thenReturn("1");
        when(managerRepo.findByEmpId(req.getManagerEmpId())).thenReturn(Optional.of(manager));
        when(teamRepo.findByName(manager.getTeamName())).thenReturn(team);

        Employee employee = service.saveEmp(req);

        //        then
        assertEquals("software_engineer".toUpperCase(), employee.getRole());
        verify(nameTrackingRepo, times(0)).save(any());
        verify(managerRepo, times(1)).findByEmpId(req.getManagerEmpId());
        verify(teamRepo, times(2)).findByName(manager.getTeamName());
    }

    @Test
    void given_employee_data_when_add_team_members_then_save() throws Exception {
        //        given
        EmployeeEntity req = EmployeeEntity.builder().name("name1 N").email("name@gmail.com").teamName("team1").gender("M").empType("F").role("software_engineer").managerEmpId("MG12").build();

        Manager manager = new Manager();
        manager.setTeamName("team1");

        Team team = new Team();
        team.setName("team1");
        ArrayList<String> members = new ArrayList<>();
        members.add("name1");
        members.add("name2");
        team.setTeamMembers(members);
        team.setTeamCount(2);

        Employee emp = new Employee();
        emp.setEmpId("N1F109");
        emp.setEmpType(req.getEmpType());
        emp.setEmail(req.getEmail());
        emp.setGender(req.getGender());
        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setRole(req.getRole());

        String[] listName = req.getName().split(" ");
        String firstName = listName[0].length() >= 2 ? listName[0] : listName[1];

        //        when
        when(nameTrackingRepo.findByFirstName(firstName)).thenReturn("1");
        when(managerRepo.findByEmpId(req.getManagerEmpId())).thenReturn(Optional.of(manager));
        when(teamRepo.findByName(manager.getTeamName())).thenReturn(team);

        Employee employee = service.saveEmp(req);

        //        then
        assertEquals("software_engineer".toUpperCase(), employee.getRole());
        assertEquals(team.getTeamMembers().size(), team.getTeamCount());
        assertEquals("team1", manager.getTeamName());
        verify(managerRepo, times(1)).findByEmpId(req.getManagerEmpId());
        verify(teamRepo, times(2)).findByName(manager.getTeamName());
    }

    @Test
    void given_employee_data_when_developer_then_no_manager_not_save() throws Exception {
        //        given
        EmployeeEntity req = EmployeeEntity.builder().name("name N").email("name@gmail.com").teamName("team1").gender("M").empType("F").role("software_engineer").managerEmpId("man1").build();

        Team team = new Team();
        team.setName("team1");
        team.setTeamMembers(new ArrayList<>());
        team.setTeamCount(0);

        Manager manager = new Manager();

        Employee emp = new Employee();
        emp.setEmpId("NEF109");
        emp.setEmpType(req.getEmpType());
        emp.setEmail(req.getEmail());
        emp.setGender(req.getGender());
        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setRole(req.getRole());

        String[] listName = req.getName().split(" ");
        String firstName = listName[0].length() >= 2 ? listName[0] : listName[1];

        //        when
        when(nameTrackingRepo.findByFirstName(firstName)).thenReturn("1");
        Throwable employee = assertThrows(EmployeeExceptions.class, () -> service.saveEmp(req));

        //        then
        assertEquals("Manager or Team Data not found", employee.getMessage());
        verify(managerRepo, times(1)).findByEmpId(req.getManagerEmpId());
        verify(empRepo, times(0)).save(emp);
    }


    @Test
    void given_employee_data_when_manager_then_save() throws Exception {
        //        given
        EmployeeEntity req = EmployeeEntity.builder().name("name N").email("name@gmail.com").teamName("team1").gender("M").empType("F").role("Manager").build();

        Manager manager = new Manager();
        manager.setTeamName("team1");

        Team team = new Team();
        team.setName("team1");
        team.setTeamMembers(new ArrayList<>());
        team.setTeamCount(0);

        Employee emp = new Employee();
        emp.setEmpId("NEF109");
        emp.setEmpType(req.getEmpType());
        emp.setEmail(req.getEmail());
        emp.setGender(req.getGender());
        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setRole(req.getRole());

        //        when
        String[] listName = req.getName().split(" ");
        String firstName = listName[0].length() >= 2 ? listName[0] : listName[1];

        //        when
        when(nameTrackingRepo.findByFirstName(firstName)).thenReturn("1");
        Employee employee = service.saveEmp(req);

        //        then
        assertEquals("Manager".toUpperCase(), employee.getRole());
        verify(teamRepo, times(0)).existsByTeamName(req.getTeamName());
    }

    @Test
    void given_employee_data_when_manager_then_create_team() throws Exception {
        //        given
        EmployeeEntity req = EmployeeEntity.builder().name("harish H").email("name@gmail.com").teamName("team1").gender("M").empType("F").role("Manager").build();

        Manager manager = new Manager();
        manager.setTeamName("team1");

        Team team = new Team();
        team.setName("team2");

        Team team2 = new Team();
        team.setName("team1");
        team.setTeamMembers(new ArrayList<>());
        team.setTeamCount(0);

        Employee emp = new Employee();
        emp.setEmpId("HHF114");
        emp.setEmpType(req.getEmpType());
        emp.setEmail(req.getEmail());
        emp.setGender(req.getGender());
        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setRole(req.getRole());

        //        when
        String[] listName = req.getName().split(" ");
        String firstName = listName[0].length() >= 2 ? listName[0] : listName[1];

        //        when
        when(nameTrackingRepo.findByFirstName(firstName)).thenReturn("1");
        teamRepo.save(team2);
        Employee employee = service.saveEmp(req);

        //        then
        assertEquals("team1", req.getTeamName());
        assertEquals("Manager".toUpperCase(), employee.getRole());
        verify(teamRepo, times(1)).save(team2);
        verify(teamRepo, times(0)).existsByTeamName(req.getTeamName());
    }

    @Test
    void given_employee_data_when_no_role_matches_exception() throws Exception {
        //given
        EmployeeEntity req = EmployeeEntity.builder().name("name N").email("name@gmail.com").teamName("team1").gender("M").empType("F").role("engineer").managerEmpId("man1").build();

        Team team = new Team();
        team.setName("team1");
        team.setTeamMembers(new ArrayList<>());
        team.setTeamCount(0);

        Employee emp = new Employee();
        emp.setEmpId("NEF109");
        emp.setEmpType(req.getEmpType());
        emp.setEmail(req.getEmail());
        emp.setGender(req.getGender());
        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setRole(req.getRole());

        //when
        EmployeeExceptions response = assertThrows(EmployeeExceptions.class, () -> service.saveEmp(req));

        //then
        assertEquals("Given Role Not found " + req.getRole(), response.getMessage());
    }

    @Test
    void shall_get_employee_data_role_software_engineer() throws JsonProcessingException {
        //given
        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        String bankDetails = "{\"accountNo\":123567,\"ifscCode\":\"IDIB0972\"}";
        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("Software_engineer");

        Address address1 = Address.builder().no("1").street("om shakti nagar").country("USA").pinCode("603031").build();
        BankDetails bankDetails1 = BankDetails.builder().accountNo("123567").ifscCode("IDIB0972").build();
        when(empRepo.findByEmpId(emp.getEmpId())).thenReturn(Optional.of(emp));

        when(empRepo.findEmployeeByEmpId(emp.getEmpId())).thenReturn(EmployeeResponseDto.builder().name("hari").empId("we123").gender("M").address(address).bankDetails(bankDetails).managerEmpId("MA012").role("Software_engineer").managerName("Mohan").build());
        when(mapper.readValue(address, Address.class)).thenReturn(address1);
        when(mapper.readValue(bankDetails, BankDetails.class)).thenReturn(bankDetails1);

        //when
        BaseEmployeeResponse response = service.getEmpResponse("we123");

        //then
        assertEquals(response.getEmployeeResponse().getName(), emp.getName());
        assertEquals("Software_engineer", response.getEmployeeResponse().getRole());
        assertNull(response.getManagerResponse());
        verify(empRepo, times(1)).findEmployeeByEmpId(emp.getEmpId());
        verify(empRepo, times(1)).findByEmpId(emp.getEmpId());
        verify(managerRepo, times(0)).findManagerByEmpId(emp.getEmpId());
    }

    @Test
    void shall_get_employee_data_role_manager() throws JsonProcessingException {
        //given
        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        String bankDetails = "{\"accountNo\":123567,\"ifscCode\":\"IDIB0972\"}";
        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("manager");

        Address address1 = Address.builder().no("1").street("om shakti nagar").country("USA").pinCode("603031").build();
        BankDetails bankDetails1 = BankDetails.builder().accountNo("123567").ifscCode("IDIB0972").build();
        when(empRepo.findByEmpId(emp.getEmpId())).thenReturn(Optional.of(emp));

        when(managerRepo.findManagerByEmpId(emp.getEmpId())).thenReturn(EmployeeResponseDto.builder().name("hari").empId("we123").gender("M").address(address).bankDetails(bankDetails).managerEmpId("MA012").role("manager").managerName("Mohan").build());
        when(mapper.readValue(address, Address.class)).thenReturn(address1);
        when(mapper.readValue(bankDetails, BankDetails.class)).thenReturn(bankDetails1);

        //when
        BaseEmployeeResponse response = service.getEmpResponse("we123");

        //then
        assertEquals(response.getManagerResponse().getName(), emp.getName());
        assertEquals("manager", response.getManagerResponse().getRole());
        assertNull(response.getEmployeeResponse());
        verify(empRepo, times(0)).findEmployeeByEmpId(emp.getEmpId());
        verify(empRepo, times(1)).findByEmpId(emp.getEmpId());
        verify(managerRepo, times(1)).findManagerByEmpId(emp.getEmpId());
    }


    @Test
    void shall_get_employee_no_data_exception() throws JsonProcessingException {
        //given
        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("manager");

        when(empRepo.findByEmpId("we1234")).thenThrow(new EmployeeExceptions("no data found"));

        //when
        EmployeeExceptions response = assertThrows(EmployeeExceptions.class, () -> service.getEmpResponse("we1234"));

        //then
        assertEquals("no data found", response.getMessage());
        verify(empRepo, times(1)).findByEmpId("we1234");
        verify(empRepo, times(0)).findEmployeeByEmpId("we1234");
    }

    @Test
    void shall_get_employee_data_json_exception() throws JsonProcessingException {
        //given
        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        String bankDetails = "{\"accountNo\":123567,\"ifscCode\":\"IDIB0972\"}";
        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("manager");

        when(empRepo.findByEmpId(emp.getEmpId())).thenReturn(Optional.of(emp));

        when(managerRepo.findManagerByEmpId(emp.getEmpId())).thenReturn(EmployeeResponseDto.builder().name("hari").empId("we123").gender("M").address(address).bankDetails(bankDetails).managerEmpId("MA012").role("manager").managerName("Mohan").build());
        when(mapper.readValue(address, Address.class)).thenThrow(JsonProcessingException.class);

        //when
        EmployeeExceptions response = assertThrows(EmployeeExceptions.class, () -> service.getEmpResponse("we123"));

        //then
        assertEquals("Json Parsing error", response.getMessage());
        verify(empRepo, times(1)).findByEmpId("we123");
        verify(empRepo, times(0)).findEmployeeByEmpId("we123");
    }

    @Test
    void shall_update_existing_employee_data() {
        //given
        String empId = "we123";

        EmployeeEntity request = EmployeeEntity.builder().email("Rhaenyra@gmail.com").role("software_engineer").teamId(2L).build();
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("we123", "hi023")).teamCount(2).managerEmpId("MA012").build();
        Team team1 = Team.builder().id(2L).name("team2").teamMembers(List.of("rh098", "ad092")).teamCount(2).managerEmpId("MA013").build();

        Employee emp = new Employee();
        emp.setEmpId("hi023");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("software_engineer");
        emp.setTeamId(team);

        Employee emp1 = new Employee();
        emp1.setEmpId(empId);
        emp1.setEmail("Rhaenyra@gmail.com");
        emp1.setRole("software_engineer");
        emp1.setTeamId(team1);

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));
        when(teamRepo.findById(request.getTeamId())).thenReturn(Optional.of(team1));
        when(teamRepo.findById(emp.getTeamId().getId())).thenReturn(Optional.of(team));
        //when
        UpdateResponse response = service.updateDetails("we123", request);
        //then
        assertEquals("Updated", response.getStatus());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(empRepo, times(1)).save(emp1);
    }

    @Test
    void shall_update_manager_data() {
        //given
        String empId = "we123";

        EmployeeEntity request = EmployeeEntity.builder().email("Rhaenyra@gmail.com").role("manager").teamId(2L).build();
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("we123", "hi023")).teamCount(2).managerEmpId("MA012").build();
        Team team1 = Team.builder().id(2L).name("team2").teamMembers(List.of("rh098", "ad092")).teamCount(2).managerEmpId(null).build();

        Employee emp = new Employee();
        emp.setEmpId("hi023");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("manager");
        emp.setTeamId(team);

        Employee emp1 = new Employee();
        emp1.setEmpId(empId);
        emp1.setEmail("Rhaenyra@gmail.com");
        emp1.setRole("manager");
        emp1.setTeamId(team1);

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));
        when(teamRepo.findById(request.getTeamId())).thenReturn(Optional.of(team1));
        when(teamRepo.findByManagerEmpId(empId)).thenReturn(team);
        //when
        UpdateResponse response = service.updateDetails("we123", request);
        //then
        assertEquals("Updated", response.getStatus());
        assertEquals(empId, team1.getManagerEmpId());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(teamRepo, times(1)).save(team);
        verify(teamRepo, times(1)).save(team1);
        verify(empRepo, times(1)).save(emp1);
    }

    @Test
    void shall_update_existing_manager_data_no_team() {
        //given
        String empId = "we123";

        EmployeeEntity request = EmployeeEntity.builder().email("Rhaenyra@gmail.com").role("manager").teamId(1L).build();
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("wa123", "hi023")).teamCount(2).managerEmpId("WE123").build();

        Employee emp = new Employee();
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("manager");
        emp.setTeamId(team);

        Employee emp1 = new Employee();
        emp1.setEmpId(empId);
        emp1.setEmail("Rhaenyra@gmail.com");
        emp1.setRole("manager");

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));
        when(teamRepo.findById(request.getTeamId())).thenReturn(Optional.of(team));
        //when
        UpdateResponse response = service.updateDetails("we123", request);
        //then
        assertEquals("Updated", response.getStatus());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(teamRepo, times(0)).save(team);
        verify(empRepo, times(1)).save(emp1);
    }

    @Test
    void shall_throw_exception_update_manager_data() {
        //given
        String empId = "we123";

        EmployeeEntity request = EmployeeEntity.builder().email("Rhaenyra@gmail.com").role("manager").teamId(2L).build();
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("we123", "hi023")).teamCount(2).managerEmpId("WE123").build();
        Team team1 = Team.builder().id(2L).name("team2").teamMembers(List.of("rh098", "ad092")).teamCount(2).managerEmpId("MA076").build();

        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("email.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("manager");
        emp.setTeamId(team);

        Employee emp1 = new Employee();
        emp1.setEmail("Rhaenyra@gmail.com");
        emp1.setRole("manager");
        emp1.setTeamId(team1);

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));
        when(teamRepo.findById(request.getTeamId())).thenReturn(Optional.of(team1));
        when(teamRepo.findByManagerEmpId(empId)).thenReturn(team);
        //when
        EmployeeExceptions response = assertThrows(EmployeeExceptions.class, () -> service.updateDetails("we123", request));
        //then
        assertEquals(" Something went wrong: Team have existing manager data " + empId.toUpperCase(), response.getMessage());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(teamRepo, times(0)).save(team);
        verify(empRepo, times(0)).save(emp1);
    }


    @Test
    void shall_no_update_existing_employee_same_data() {
        //given
        String empId = "we123";

        EmployeeEntity request = EmployeeEntity.builder().name("hari").empType("f").gender("M").teamId(1L).email("Rhaenyra@gmail.com").role("software_engineer").build();
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("Rhaenyra")).teamCount(1).managerEmpId("MA012").build();

        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("Rhaenyra@gmail.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setRole("software_engineer");
        emp.setTeamId(team);

        Employee emp1 = new Employee();
        emp1.setEmpId("we123");
        emp1.setEmail("Rhaenyra@gmail.com");
        emp1.setRole("software_engineer");
        emp1.setTeamId(team);

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));

        //when
        UpdateResponse response = service.updateDetails("we123", request);
        //then
        assertEquals("Skipped", response.getStatus());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(empRepo, times(0)).save(emp1);
    }

    @Test
    void shall_update_employee_json_exception() throws JsonProcessingException {
        //given
        String empId = "we123";
        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        String bankDetails = "{\"accountNo\":123567,\"ifscCode\":\"IDIB0972\"}";
        EmployeeEntity request = EmployeeEntity.builder().name("hari").empType("f").gender("M").teamId(1L).email("Rhaenyra@gmail.com").role("software_engineer").build();
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("Rhaenyra")).teamCount(1).managerEmpId("MA012").build();

        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("Rhaenyra@gmail.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setAddress(address);
        emp.setRole("software_engineer");
        emp.setTeamId(team);

        Employee emp1 = new Employee();
        emp1.setEmpId("we123");
        emp1.setEmail("Rhaenyra@gmail.com");
        emp1.setRole("software_engineer");
        emp1.setTeamId(team);

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));
        when(mapper.readValue(address, Address.class)).thenThrow(JsonProcessingException.class);

        //when
        EmployeeExceptions response = assertThrows(EmployeeExceptions.class, () -> service.updateDetails("we123", request));
        //then
        assertTrue(response.getMessage().contains(" Something went wrong"));
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(empRepo, times(0)).save(emp1);
    }

    @Test
    void shall_get_all_employee() {
        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";

        Employee emp = new Employee();
        emp.setEmpId("we123");
        emp.setEmpType("f");
        emp.setEmail("Rhaenyra@gmail.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setAddress(address);
        emp.setRole("software_engineer");

        when(empRepo.findAll()).thenReturn(List.of(emp));
        service.getAllEmpResponse();

        verify(empRepo, times(1)).findAll();
    }

    @Test
    void shall_throw_exception_all_employee() {
        when(empRepo.findAll()).thenReturn(List.of());
        EmployeeExceptions exceptions = assertThrows(EmployeeExceptions.class, () -> service.getAllEmpResponse());
        assertEquals("No Data found", exceptions.getMessage());
        verify(empRepo, times(1)).findAll();
    }

    @Test
    void shall_update_employee_status() {

        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("we123")).teamCount(1).managerEmpId("MA012").build();

        String empId = "we123";

        Employee emp = new Employee();
        emp.setEmpId(empId);
        emp.setEmpType("f");
        emp.setEmail("Rhaenyra@gmail.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setAddress(address);
        emp.setRole("software_engineer");
        emp.setTeamId(team);

        when(statusConfiguration.getStatusValues()).thenReturn(List.of("remove", "delete"));
        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));
        when(teamRepo.findById(team.getId())).thenReturn(Optional.of(team));

        service.updateStatus(empId, "remove");

        assertNull(emp.getTeamId());
        assertFalse(emp.isActive());
        assertTrue(team.getTeamMembers().isEmpty());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(teamRepo, times(1)).findById(1L);
    }


    @Test
    void shall_update_employee_status_delete() {

        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("df123")).teamCount(1).managerEmpId("MA012").build();

        String empId = "we123";

        Employee emp = new Employee();
        emp.setEmpId(empId);
        emp.setEmpType("f");
        emp.setEmail("Rhaenyra@gmail.com");
        emp.setGender("M");
        emp.setName("hari");
        emp.setAddress(address);
        emp.setRole("software_engineer");
        emp.setActive(false);
        emp.setDeleted(false);
        when(statusConfiguration.getStatusValues()).thenReturn(List.of("remove", "delete"));

        when(empRepo.findByEmpId(empId)).thenReturn(Optional.of(emp));

        service.updateStatus(empId, "delete");

        assertNull(emp.getTeamId());
        assertFalse(emp.isActive());
        assertFalse(team.getTeamMembers().contains(empId));
        assertTrue(emp.isDeleted());
        verify(empRepo, times(1)).findByEmpId(empId);
        verify(teamRepo, times(0)).findById(1L);

    }

    @Test
    void shall_update_employee_status_invalid_exception() {


        String empId = "we123";
        String status = "removed";
        when(statusConfiguration.getStatusValues()).thenReturn(List.of("remove", "delete"));
        EmployeeExceptions exceptions = assertThrows(EmployeeExceptions.class, () -> service.updateStatus(empId, status));

        assertEquals(exceptions.getMessage(), "Given status is not valid: " + status);
        verify(empRepo, times(0)).findByEmpId(empId);
        verify(teamRepo, times(0)).findById(1L);

    }

    @Test
    void shall_update_employee_status_invalid() {

        String address = "{\"no\":1,\"street\":\"om shakti nagar\",\"pinCode\":603301,\"country\":\"USA\"}";
        Team team = Team.builder().id(1L).name("team1").teamMembers(List.of("df123")).teamCount(1).managerEmpId("MA012").build();

        String empId = "we123";
        String status = "remove";
        when(statusConfiguration.getStatusValues()).thenReturn(List.of("remove", "delete"));
        when(empRepo.findByEmpId(empId)).thenThrow(new EmployeeExceptions("Employee not found EmpId: "+empId));
        EmployeeExceptions exceptions = assertThrows(EmployeeExceptions.class, () -> service.updateStatus(empId, status));
        assertEquals(exceptions.getMessage(), "Employee not found EmpId: " + empId);
        verify(empRepo,times(1)).findByEmpId(empId);
        verify(teamRepo,times(0)).findById(1L);
    }

}

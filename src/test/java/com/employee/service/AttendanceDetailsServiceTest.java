package com.employee.service;

import com.employee.entity.request.AttendanceStatusRequest;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.AttendanceDetails;
import com.employee.model.Employee;
import com.employee.model.Team;
import com.employee.repository.AttendanceDetailsRepo;
import com.employee.repository.EmployeeRepo;
import com.employee.repository.TeamRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AttendanceDetailsServiceTest {

    @InjectMocks
    private AttendanceDetailsService service;

    @Mock
    private EmployeeRepo empRepo;

    @Mock
    private AttendanceDetailsRepo repo;
    @Mock
    private TeamRepo teamRepo;

    @Test
    void shall_test_employee_attendance() {
//    given
        Team team = Team.builder().id(1L).name("team 1").teamMembers(List.of("we123")).teamCount(1).build();
        Employee employee = new Employee();
        employee.setActive(true);
        employee.setTeamId(team);
        AttendanceStatusRequest request = AttendanceStatusRequest.builder().employeeId("we123").status("wfh").build();
//    when
        when(repo.findByEmpId(request.getEmployeeId())).thenReturn(Optional.empty());
        when(empRepo.findByEmpId("we123")).thenReturn(Optional.of(employee));
        when(teamRepo.findById(1L)).thenReturn(Optional.ofNullable(Team.builder().teamCount(1).teamMembers(List.of("we123")).managerEmpId("we124").build()));
        String res = service.insertAttendance(request);
//    then
        Assertions.assertEquals("Employee Attendance Added", res);
        verify(repo, times(1)).findByEmpId("we123");
        verify(teamRepo, times(1)).findById(1L);
        verify(empRepo, times(1)).findByEmpId("we123");
    }

    @Test
    void shall_test_employee_attendance_throw_exception() {
//    given
        Team team = Team.builder().id(1L).name("team 1").teamMembers(List.of("we123")).teamCount(1).build();
        Employee employee = new Employee();
        employee.setActive(true);
        employee.setTeamId(team);
        AttendanceStatusRequest request = AttendanceStatusRequest.builder().employeeId("we123").status("wfh").build();
//    when
        when(repo.findByEmpId(request.getEmployeeId())).thenThrow(new EmployeeExceptions("No employee found"));

        EmployeeExceptions res = assertThrows(EmployeeExceptions.class, () -> service.insertAttendance(request));
//    then
        Assertions.assertEquals("No employee found", res.getMessage());
        verify(repo, times(1)).findByEmpId("we123");
        verify(teamRepo, times(0)).findById(1L);
        verify(empRepo, times(0)).findByEmpId("we123");
    }

    @Test
    void shall_test_employee_attendance_invalid_status_throw_exception() {
        //    given
        Team team = Team.builder().id(1L).name("team 1").teamMembers(List.of("we123")).teamCount(1).build();
        Employee employee = new Employee();
        employee.setActive(true);
        employee.setTeamId(team);
        AttendanceStatusRequest request = AttendanceStatusRequest.builder().employeeId("we123").status("wf").build();
        //    when

        EmployeeExceptions res = assertThrows(EmployeeExceptions.class, () -> service.insertAttendance(request));
        //    then
        Assertions.assertTrue(res.getMessage().contains("Should Match with [WFH, WFO, OOO]"));
        verifyNoInteractions(repo, teamRepo, empRepo);
        verify(repo, times(0)).findByEmpId("we123");
        verify(teamRepo, times(0)).findById(1L);
        verify(empRepo, times(0)).findByEmpId("we123");
    }

    @Test
    void shall_test_update_employee_attendance() {
        //    given
        Team team = Team.builder().id(1L).name("team 1").teamMembers(List.of("we123")).teamCount(1).build();
        Employee employee = new Employee();
        employee.setActive(true);
        employee.setTeamId(team);
        AttendanceStatusRequest request = AttendanceStatusRequest.builder().employeeId("we123").status("wfo").build();
        //    when

        when(repo.findByEmpId(request.getEmployeeId())).thenReturn(Optional.of(AttendanceDetails.builder().empId("we123").mEmpId("we124").ooo(0).wfh(1).wfo(0).updatedAt(Timestamp.valueOf("2025-03-03 15:15:40.0")).build()));

        String res = service.insertAttendance(request);
//    then
        Assertions.assertEquals("Employee Attendance Updated", res);
        verify(repo, times(1)).findByEmpId("we123");
    }

    @Test
    void shall_test_update_employee_attendance_throw_exception() {
        //    given
        Team team = Team.builder().id(1L).name("team 1").teamMembers(List.of("we123")).teamCount(1).build();
        Employee employee = new Employee();
        employee.setActive(true);
        employee.setTeamId(team);
        AttendanceStatusRequest request = AttendanceStatusRequest.builder().employeeId("we123").status("wfo").build();
        //    when

        when(repo.findByEmpId(request.getEmployeeId())).thenReturn(Optional.of(AttendanceDetails.builder().empId("we123").mEmpId("we124").ooo(0).wfh(1).wfo(0).updatedAt(Timestamp.valueOf(LocalDateTime.now())).build()));

        EmployeeExceptions res = assertThrows(EmployeeExceptions.class, () -> service.insertAttendance(request));
//    then
        Assertions.assertEquals("Attendance Already Updated", res.getMessage());
        verify(repo, times(1)).findByEmpId("we123");
    }
}

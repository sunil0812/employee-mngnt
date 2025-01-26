package com.employee.service;

import com.employee.constants.AttendanceDetailsConstant;
import com.employee.entity.request.AttendanceStatusRequest;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.AttendanceDetails;
import com.employee.model.Employee;
import com.employee.model.Team;
import com.employee.repository.AttendanceDetailsRepo;
import com.employee.repository.EmployeeRepo;
import com.employee.repository.TeamRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class AttendanceDetailsService {

    @Autowired
    private AttendanceDetailsRepo repo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private TeamRepo teamRepo;

    public String insertAttendance(AttendanceStatusRequest details) {
        validateStatus(details.getStatus());
        Optional<AttendanceDetails> attendanceDetail = repo.findByEmpId(details.getEmployeeId());
        return attendanceDetail.map(attendanceDetails -> updateAttendanceEmployee(attendanceDetails, details.getStatus())).orElseGet(() -> insertEmployeeAttendance(details));
    }

    private String updateAttendanceEmployee(AttendanceDetails attendanceDetail, String status) {
       AttendanceDetails details = addAttendanceStatus(attendanceDetail, status);
        repo.save(details);
        return "Employee Attendance Updated";
    }

    private AttendanceDetails validateEmployee(String employeeId) {
        Employee employee = employeeRepo.findByEmpId(employeeId).orElseThrow(() -> new EmployeeExceptions("No Employee found for ID: " + employeeId));
        Team team = teamRepo.findById(employee.getTeamId().getId()).orElseThrow(() -> new EmployeeExceptions("No team found for Employee "));
        return AttendanceDetails.builder().empId(employeeId).mEmpId(team.getManagerEmpId()).teamId(team.getId()).build();
    }

    private void validateStatus(String status) {
        if (!AttendanceDetailsConstant.WFO.name().equals(status.toUpperCase()) &&
                !AttendanceDetailsConstant.WFH.name().equals(status.toUpperCase()) &&
                !AttendanceDetailsConstant.OOO.name().equals(status.toUpperCase())) {
            throw new EmployeeExceptions("Invalid Status: " + status + " Should Match with " + Arrays.toString(AttendanceDetailsConstant.values()));
        }
    }

    private AttendanceDetails addAttendanceStatus(AttendanceDetails attendance, String status) {
        if (Objects.equals(AttendanceDetailsConstant.WFO.name(), status.toUpperCase())) {
            int wfo = attendance.getWfo();
            attendance.setWfo(wfo + 1);
        } else if (Objects.equals(AttendanceDetailsConstant.WFH.name(), status.toUpperCase())) {
            int wfh = attendance.getWfh();
            attendance.setWfh(wfh + 1);
        } else if(Objects.equals(AttendanceDetailsConstant.OOO.name(), status.toUpperCase())){
            int ooo = attendance.getOoo();
            attendance.setOoo(ooo + 1);
        }
        return attendance;
    }

    private String insertEmployeeAttendance(AttendanceStatusRequest request){
        log.info("Inserting attendance new data");
        AttendanceDetails attendance = validateEmployee(request.getEmployeeId());
        AttendanceDetails attendanceDetails = addAttendanceStatus(attendance, request.getStatus());
          repo.save(attendanceDetails);
          return "Employee Attendance Added";
    }
}

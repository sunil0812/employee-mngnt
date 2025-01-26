package com.employee.repository;

import com.employee.model.AttendanceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendanceDetailsRepo extends JpaRepository<AttendanceDetails, String> {
    @Query(value = "SELECT * FROM employee_message.attendance_details where emp_id= :empId ", nativeQuery = true)
    Optional<AttendanceDetails> findByEmpId(@Param("empId") String empId);
}

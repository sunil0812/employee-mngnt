package com.employee.repository;

import com.employee.entity.dto.EmployeeResponseDto;
import com.employee.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagerRepo extends JpaRepository<Manager, Long> {

    @Query(value = "select * from employee_message.manager_data where name = :manager",nativeQuery = true)
    Manager findbyName(@Param("manager") String manager);

    @Query(value = "select * from employee_message.manager_data where emp_id = :empId",nativeQuery = true)
    Optional<Manager> findByEmpId(@Param("empId") String empId);


    @Query("SELECT new com.employee.entity.dto.EmployeeResponseDto(e.name, e.empId, e.role, e.email, e.gender, e.phone,t.name, e.address,e.bankDetails,t.teamMembers) FROM Manager m JOIN Team t ON t.managerEmpId = m.empId JOIN Employee e ON m.empId = e.empId WHERE e.empId = ?1")
    EmployeeResponseDto findManagerByEmpId(String empId);


}

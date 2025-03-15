package com.employee.repository;

import com.employee.entity.dto.EmployeeResponseDto;
import com.employee.entity.dto.TeamResponseDto;
import com.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee, String> {
    @Query(value = "SELECT * FROM employee_message.employees_data where emp_id= :empId and deleted = false ", nativeQuery = true)
    Optional<Employee> findByEmpId(@Param("empId") String empId);


    @Query("SELECT new com.employee.entity.dto.EmployeeResponseDto(e.name, e.empId, e.role, e.email, e.gender, e.dob, e.phone,t.name, e.address,e.bankDetails,m.empId,m.name) FROM Employee e JOIN Team t ON t.id = CAST(e.teamId as integer) JOIN Manager m ON m.empId = t.managerEmpId WHERE e.empId = ?1 AND e.deleted = false")
    EmployeeResponseDto findEmployeeByEmpId(String empId);

    @Query(value = "SELECT * FROM employee_message.employees_data where deleted = false ", nativeQuery = true)
    List<Employee> findAll();


    @Query(value = "SELECT COUNT(*) > 0 FROM employee_message.employees_data where phone = :phone", nativeQuery = true)
    boolean existsByPhone(@Param("phone") String phone);

    @Query(value = "SELECT COUNT(*) > 0 FROM employee_message.employees_data where email = :mail", nativeQuery = true)
    boolean existsByEmail(@Param("mail") String mail);

}

package com.employee.repository;

import com.employee.model.EmployeeNameTracking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeNameTrackingRepo extends JpaRepository<EmployeeNameTracking, Long> {
    @Query(value = "select name_count from employee_message.employees_name_tracking where first_name = :firstName ", nativeQuery = true)
    String findByFirstName(@Param("firstName") String firstName);

    @Transactional
    @Modifying
    @Query(value = "UPDATE employee_message.employees_name_tracking SET name_count = :count WHERE first_name = :firstName", nativeQuery = true)
    void updateNameCount(@Param("count") int count, @Param("firstName") String firstName);
}

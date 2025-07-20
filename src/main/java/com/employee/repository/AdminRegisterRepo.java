package com.employee.repository;

import com.employee.model.AdminRegister;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRegisterRepo extends JpaRepository<AdminRegister, Long> {

    boolean existsByPhone(@NotNull String value);

    boolean existsByEmail(@NotNull String value);

    boolean existsByName(@NotNull String value);

    @Query(value = "SELECT EXISTS (  SELECT 1  FROM admin_register.admin_data  WHERE CAST(company_details AS jsonb) ->> 'name' = :companyName)", nativeQuery = true)
    boolean existsByCompanyName(@Param("companyName") String companyName);
}

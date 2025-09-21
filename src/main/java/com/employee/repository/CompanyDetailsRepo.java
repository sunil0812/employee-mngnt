package com.employee.repository;

import com.employee.model.CompanyDetails;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyDetailsRepo extends JpaRepository<CompanyDetails,Long> {
    @Query(value = "select * from admin_register.company_details where id= :id",nativeQuery = true)
    CompanyDetails getByCompanyId(@Param("id")Long id);

    boolean existsByName(@NotNull String value);

    boolean existsByDomain(@NotNull String value);

    boolean existsByPhone(@NotNull String value);
}

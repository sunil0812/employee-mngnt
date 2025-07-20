package com.employee.repository;

import com.employee.model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyDetailsRepo extends JpaRepository<CompanyDetails,Long> {
    @Query(value = "select * from admin_register.company_details where id= :id",nativeQuery = true)
    CompanyDetails getByCompanyId(@Param("id")Long id);
}

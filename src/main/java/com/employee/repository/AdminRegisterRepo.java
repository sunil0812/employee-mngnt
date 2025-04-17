package com.employee.repository;

import com.employee.model.AdminRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRegisterRepo extends JpaRepository<AdminRegister, Long> {
}

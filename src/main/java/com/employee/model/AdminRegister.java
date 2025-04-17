package com.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@Table(schema = "admin_register", name = "admin_data")
public class AdminRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String address;
    private String bankDetails;
    private String companyDetails;
    private String teamDetails;
    private boolean isActive;
    private Timestamp createdAt;
}

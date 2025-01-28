package com.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(schema = "employee_message", name = "attendance_details")
public class AttendanceDetails {
    @Id
    private String empId;
    private String mEmpId;
    private Long teamId;
    private int wfo;
    private int wfh;
    private int ooo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

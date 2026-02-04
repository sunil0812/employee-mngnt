package com.employee.model;




import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.sql.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "employee_message", name = "employees_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    @Id
    private String empId;
    private String name;
    private String role;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String empType;
    private String bankDetails;
    private String address;
    private boolean isActive;
    private boolean deleted;
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team teamId;
    @ManyToOne
    @JoinColumn(name = "company_details", referencedColumnName = "id")
    private CompanyDetails companyDetails;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String password;
    private String additionalInfo;



    public Employee(String name,String role,String gender, String email,String phone,String empType,String dob){
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.empType = empType;
    }

    public static String empIdByName(String firstName, int index, String initial, int value) {

        return String.valueOf(firstName.charAt(0)).toUpperCase() + firstName.substring(0, index).toUpperCase() + initial.toUpperCase() + value;
    }

    public static String empIdFirstName(String firstName, String initial, int value) {

        return String.valueOf(firstName.charAt(0)).toUpperCase() + initial.toUpperCase() + value;
    }

}

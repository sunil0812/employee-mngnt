package com.employee.bdd;

import com.employee.entity.request.AttendanceStatusRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.checkerframework.checker.index.qual.SameLen;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AttendanceDetailsSteps extends BaseSteps {

    private ValidatableResponse response;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper mapper;


    @When("employee {string} attendance not already stored")
    public void checkEmployeeAttendance(String empId) throws SQLException {
        String query = "select * from employee_message.attendance_details where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
        }
        assertEquals(0, rowCount);
    }


    @Given("employee id {string} and status {string} of attendance")
    public void saveAttendanceDetails(String empId, String status) {
        RestAssured.baseURI = "http://localhost:8080";
        AttendanceStatusRequest request = AttendanceStatusRequest.builder().employeeId(empId).status(status).build();
        response = RestAssured.given()
                .log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when().put("/attendance/save")
                .then()
                .log().all();
    }

    @Then("validate the attendance of employee {string}")
    public void validateSaveAttendance(String empId) throws SQLException {
        String query = "select * from employee_message.attendance_details where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
        }
        assertEquals(1, rowCount);
    }

    @Then("validate the employee {string} attendance of already stored in db with {int}")
    public void validateAlreadySavedAttendance(String empId, int statusCode) throws SQLException {
        String query = "select * from employee_message.attendance_details where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
        }
        assertEquals(500,statusCode);
        assertEquals(1, rowCount);
    }


    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

}

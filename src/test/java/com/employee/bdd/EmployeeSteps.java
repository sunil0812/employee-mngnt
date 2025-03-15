package com.employee.bdd;

import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.response.BaseEmployeeResponse;
import com.employee.entity.response.TeamData;
import com.employee.entity.response.UpdateResponse;
import com.employee.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class EmployeeSteps extends BaseSteps {


    private ValidatableResponse response;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper mapper;

    @When("passing valid employee request for role {string}")
    public void employee_details_request(String role) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        EmployeeEntity request = mapper.readValue(new File("src/test/resources/stubs/insert/" + role.toLowerCase() + "_request.json"), EmployeeEntity.class);

        request = request.getRole().contains("manager") ? request : getManagerId(request);

        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/emp/save")
                .then()
                .log().all();

    }

    private EmployeeEntity getManagerId(EmployeeEntity request) {
        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .when().get("/team/teamName")
                .then()
                .log().all();
        List<TeamData> teamList = response.extract().response().as(new TypeRef<List<TeamData>>() {
        });
        String mEmpId = teamList.get(0).getManagerId();
        String teamName = teamList.get(0).getName();
        return request.toBuilder().managerEmpId(mEmpId).teamName(teamName).build();
    }

    @Then("Store the employee details in DB")
    public void store_data_db() throws JsonProcessingException, SQLException {
        String empResponse = response.extract().body().asPrettyString();
        Employee emp = mapper.readValue(empResponse, Employee.class);

        String query = "select * from employee_message.employees_data where emp_id ='" + emp.getEmpId() + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                String role = resultSet.getString("role");
                String empId = resultSet.getString("emp_id");
                String name = resultSet.getString("name");
                assertEquals(role, emp.getRole());
                assertEquals(empId, emp.getEmpId());
                assertEquals(name, emp.getName());
                rowCount++;
            }
            assertEquals(1, rowCount);
        }
    }

    @Given("check id {string} already presented in database")
    public void checkInDbById(String empId) throws SQLException {
        String query = "select * from employee_message.employees_data where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                checkInTeamTable(empId, resultSet.getInt("team_id"));
                rowCount++;
            }
        }
        assertEquals(1, rowCount);
    }

    @Given("id {string} should available in database")
    public void checkInDbByEmpId(String empId) throws SQLException {
        String query = "select * from employee_message.employees_data where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
        }
        assertEquals(1, rowCount);
    }

    private void checkInTeamTable(String empId, int teamId) throws SQLException {
        String query = "select * from employee_message.team_data where id='" + teamId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                Array value = resultSet.getArray("team_members");
                String[] members = (String[]) value.getArray();
                List<String> values = List.of(members);
                assertTrue(members.length > 0);
                assertTrue(values.contains(empId));
                rowCount++;
            }
        }
        assertEquals(1, rowCount);
    }

    @When("get details for employee id {string}")
    public void getEmployeeDetails(String empId) {

        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .when().get("emp/getEmployee/{empId}", empId)
                .then()
                .log().all();
    }

    @Then("validate the id {string} details with {string} for role {string}")
    public void validateResponse(String empId, String teamName, String role) throws SQLException, JsonProcessingException {
        String value = response.extract().body().asPrettyString();
        BaseEmployeeResponse actualResponse = mapper.readValue(value, BaseEmployeeResponse.class);
        assertEquals(200, response.extract().statusCode());
        String query = "select * from employee_message.employees_data where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
                String id = resultSet.getString("emp_id");
                String actualRole = resultSet.getString("role");
                assertEquals(role.toUpperCase(), actualRole);
                assertEquals(empId, id);
                if (actualRole.equals("MANAGER")) {
                    assertNull(actualResponse.getEmployeeResponse());
                    assertNotNull(actualResponse.getManagerResponse().getTeamData());
                    assertEquals(actualResponse.getManagerResponse().getTeamData().getName(), teamName);
                } else {
                    assertNull(actualResponse.getManagerResponse());
                    assertNotNull(actualResponse.getEmployeeResponse().getManager());
                    assertEquals(actualResponse.getEmployeeResponse().getTeamName(), teamName);
                }

            }
        }
        assertEquals(1, rowCount);
    }

    @When("employee id {string} removed from a team with status {string}")
    public void updateEmployee(String empId, String status) {

        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .when().put("emp/update/{empId}/{status}", empId, status)
                .then()
                .log().all();
    }

    @Then("validate the id {string} details with removed team")
    public void validateUpdatedEmpResponse(String empId) throws SQLException, JsonProcessingException {
        String value = response.extract().body().asPrettyString();

        assertEquals(200, response.extract().statusCode());
        String query = "select * from employee_message.employees_data where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
                int team = resultSet.getInt("team_id");
                assertNotEquals(team, 1);
                assertEquals(" Employee Successfully Removed From Team EmpId: " + empId, value);
            }
        }

        assertEquals(1, rowCount);
    }

    @When("update details for employee id {string} with team")
    public void updateEmployeeDetails(String empId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EmployeeEntity request = mapper.readValue(new File("src/test/resources/stubs/update/" + empId.toLowerCase() + "_request.json"), EmployeeEntity.class);

        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("emp/update/{empId}", empId)
                .then()
                .log().all();
    }

    @Then("validate the id {string} details with email as {string} and team")
    public void validateUpdateEmpResponse(String empId, String email) throws SQLException, JsonProcessingException {
        String value = response.extract().body().asPrettyString();
        UpdateResponse actualResponse = mapper.readValue(value, UpdateResponse.class);
        assertEquals(200, response.extract().statusCode());
        String query = "select * from employee_message.employees_data where emp_id='" + empId + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
                String mail = resultSet.getString("email");
                int teamId = resultSet.getInt("team_id");
                checkInTeamTable(empId, teamId);
                assertEquals(mail, email);
                assertEquals(1, teamId);
                assertEquals("Updated", actualResponse.getStatus());
            }
        }
        assertEquals(1, rowCount);
    }

    @Then("validate the update details as {string}")
    public void validateUpdateEmpResponseSkipped(String status) throws JsonProcessingException {
        String value = response.extract().body().asPrettyString();
        UpdateResponse actualResponse = mapper.readValue(value, UpdateResponse.class);
        assertEquals(actualResponse.getStatus(),status);
        assertEquals(200, response.extract().statusCode());
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

}

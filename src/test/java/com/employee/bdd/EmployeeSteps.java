package com.employee.bdd;

import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.response.BaseEmployeeResponse;
import com.employee.entity.response.ManagerResponse;
import com.employee.entity.response.TeamData;
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
import io.zonky.test.db.flyway.FlywayWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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
        EmployeeEntity request = mapper.readValue(new File("src/test/resources/stubs/" + role.toLowerCase() + "_request.json"), EmployeeEntity.class);

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
        System.out.println(" response" + actualResponse);
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

    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public String readJsonFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }
}

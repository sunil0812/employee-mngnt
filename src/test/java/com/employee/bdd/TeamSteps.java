package com.employee.bdd;

import com.employee.entity.response.TeamData;
import com.employee.model.Team;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TeamSteps extends BaseSteps {

    private ValidatableResponse response;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper mapper;


    @Given("team name {string} should be save in db")
    public void saveTeamDB(String teamName) {
        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.TEXT)
                .body(teamName)
                .when().post("/team/saveTeam")
                .then()
                .log().all();
    }

    @Then("validate the team details with {int}")
    public void validateTeamResponse(int statusCode) throws JsonProcessingException, SQLException {
        String teamValue = response.extract().body().asPrettyString();
        Team team = mapper.readValue(teamValue, Team.class);

        String query = "select * from employee_message.team_data where name ='" + team.getName() + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                int teamCount = resultSet.getInt("team_count");
                assertEquals(0, teamCount);
                rowCount++;
            }
        }
        assertEquals(response.extract().statusCode(), statusCode);
        assertEquals(1, rowCount);
    }

    @Then("validate already exist with {int}")
    public void validateTeamResponses(int statusCode) throws JsonProcessingException, SQLException {
        String teamValue = response.extract().body().asPrettyString();
        Team team = mapper.readValue(teamValue, Team.class);

        String query = "select * from employee_message.team_data where name ='" + team.getName() + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                int teamCount = resultSet.getInt("team_count");
                assertEquals(0, teamCount);
                rowCount++;
            }
        }
        assertEquals(response.extract().statusCode(), statusCode);
        assertEquals(0, rowCount);
    }

    @When("team details with manager id in a list")
    public void getTeamName() {
        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .when().get("/team/teamName")
                .then()
                .log().all();
    }

    @Then("validate the list can be null or not null with {int}")
    public void validateTeamNameResponses(int statusCode) throws JsonProcessingException, SQLException {
        String teamValue = response.extract().body().asPrettyString();
        List<TeamData> team = mapper.readValue(teamValue, new TypeReference<List<TeamData>>() {});
        String query = "select * from employee_message.team_data where is_active=true AND deleted=false";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
        }
        assertEquals(team.size(), rowCount);
        assertEquals(response.extract().statusCode(), statusCode);
    }

    @When("team details are available")
    public void getTeamDetails() {
        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .when().get("/team/getTeam")
                .then()
                .log().all();
    }

    @Then("validate the team details list with {int}")
    public void validateTeamDetailsResponses(int statusCode) throws JsonProcessingException, SQLException {
        String teamValue = response.extract().body().asPrettyString();
        List<Team> team = mapper.readValue(teamValue, new TypeReference<List<Team>>() {});
        String query = "select * from employee_message.team_data where is_active=true AND deleted=false";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
        }
        assertEquals(team.size(), rowCount);
        assertEquals(response.extract().statusCode(), statusCode);
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }
}

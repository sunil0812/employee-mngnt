package com.employee.bdd;

import com.employee.entity.request.AdminRegisterRequest;
import com.employee.model.ValidateDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.type.TypeReference;


import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static wiremock.org.hamcrest.MatcherAssert.assertThat;

public class AdminRegisterSteps extends BaseSteps{


    private ValidatableResponse response;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper mapper;

    @When("{string} of {string} request to store in our system")
    public void saveAdminDetails(String value, String valid) throws IOException {
    AdminRegisterRequest request =  mapper.readValue(new File("src/test/resources/stubs/request/admin_details_"+valid + ".json"), AdminRegisterRequest.class);
        List<ValidateDetails> validate = mapper.readValue(new File("src/test/resources/stubs/request/admin_" + valid + ".json"), new TypeReference<List<ValidateDetails>>() {});

        validate.forEach(e -> {
                getResponse(e);
                if (response.extract().response().body().asPrettyString().contains(" Already taken "))
                assertTrue(response.extract().response().body().asPrettyString().contains(" Validated "));
        });


        RestAssured.baseURI = "http://localhost:8080";
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/register/admin")
                .then()
                .log().all();

    }

    @Then("store the admin info in our system {string}")
    public void storeAdminDetails(String value) throws SQLException {

        String query = "select * from admin_register.admin_data where name ='" + value + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
            assertEquals(1, rowCount);
            assertEquals(200,response.extract().statusCode());
        }
    }

    @Then("validation failed {string} and throws {string}")
    public void inValidAdmin(String name, String value) throws SQLException,JsonProcessingException {

        String query = "select * from admin_register.admin_data where name ='" + name + "'";
        int rowCount = 0;
        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                rowCount++;
            }
            String responses=response.extract().response().asPrettyString();

            assertEquals(0, rowCount);
            assertEquals(500,response.extract().statusCode());
            assertEquals(value,mapper.readValue(responses,String.class));
        }

    }



    public void getResponse(Object request){
        response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/register/validate-details")
                .then()
                .log().all();
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }
//    private ValidateDetails getValidateDetails(String valid) {
//        return mapper.readValue(new File("src/test/resources/stubs/request/admin_" +valid + ".json"),ValidateDetails.class);
//    }
//
//    private AdminRegister getAdminDetails(String value, String valid) throws IOException {
//
//        return mapper.readValue(new File("src/test/resources/stubs/request/" + value+valid + ".json"),AdminRegister.class);
//    }
}

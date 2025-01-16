package com.employee.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

public abstract class BaseSteps {


    private final static String BASE_URI="http://localhost:8080";

    @Autowired
    private ObjectMapper mapper;

    private void configureRestAssured(){
        RestAssured.baseURI=BASE_URI;
    }


    protected RequestSpecification requestSpecification(){
        configureRestAssured();
        return given();
    }

}

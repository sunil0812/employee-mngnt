package com.employee.bdd.config;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
//        WireMockServer ser  = new WireMockServer(8080);
//        ser.start();
//        WireMock.configureFor("localhost", 8080);
    }
}

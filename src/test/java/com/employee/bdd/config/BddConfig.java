package com.employee.bdd.config;

import com.employee.EmployeeMgmntApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

@CucumberContextConfiguration
@SpringBootTest(classes = {EmployeeMgmntApplication.class},
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES, provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY, refresh = AutoConfigureEmbeddedDatabase.RefreshMode.NEVER)
@AutoConfigureMockMvc
@ActiveProfiles({"application-test"})
@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(initializers = {WireMockInitializer.class}, classes = {EmployeeMgmntApplication.class})
public class BddConfig {


    @TestConfiguration
    @Profile("application-test")
    public static class TestConfig{

        @Autowired
        private WireMockServer wireMock;
    }


}

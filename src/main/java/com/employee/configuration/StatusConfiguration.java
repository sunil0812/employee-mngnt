package com.employee.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "status")
@Getter
@Setter
public class StatusConfiguration {

private List<String> statusValues;


}

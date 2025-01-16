package com.employee.entity.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamResponse {

    private Long id;
    private String name;
    private List<String> members;
    private String managerEmpId;
    private int teamCount;
    private String managerName;

}

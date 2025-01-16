package com.employee.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class TeamResponseDto {

    private Long id;
    private String name;
    private List<String> members;
    private String managerEmpId;
    private int teamCount;
    private String managerName;


    public TeamResponseDto(Long id, String name, List<String> members, String managerEmpId, int teamCount, String managerName) {
        this.id = id;
        this.name = name;
        this.managerEmpId = managerEmpId;
        this.managerName = managerName;
        this.teamCount = teamCount;
        this.members = members;
    }
}

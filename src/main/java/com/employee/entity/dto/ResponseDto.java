package com.employee.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private String name;
    private String empId;
    private String teamName;
    private String managerName;

    public ResponseDto(String name,String empId,String teamName,String managerName){
        this.empId = empId;
        this.name = name;
        this.teamName = teamName;
        this.managerName = managerName;
    }
}

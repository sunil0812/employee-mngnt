package com.employee.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    private String name;
    private String managerEmpId;
    private int teamCount;
    private List<String> teamMembers;
}

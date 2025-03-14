package com.employee.controller;

import com.employee.entity.request.TeamRequest;
import com.employee.entity.response.TeamData;
import com.employee.entity.response.TeamResponse;
import com.employee.model.Team;
import com.employee.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;


@ExtendWith(MockitoExtension.class)
class TeamControllerTest {
    @InjectMocks
    private TeamController controller;

    @Mock
    private TeamService service;

    @Test
    void shall_test_save_team_data() {
        TeamRequest request = TeamRequest.builder().name("team1").managerEmpId("we123").build();
        Team team = Team.builder().teamCount(0).teamMembers(List.of()).managerEmpId("we123").name("team1").build();
        Mockito.when(service.saveTeamDetails("team1")).thenReturn(team);
        ResponseEntity<Team> response = controller.saveTeam("team1");
        Assertions.assertEquals("team1", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void shall_test_get_team_data() {

        TeamResponse teamResponse = TeamResponse.builder().teamCount(0).name("team2").managerName("john").managerEmpId("JH123").members(List.of()).build();
        Mockito.when(service.getTeamData(1)).thenReturn(teamResponse);
        ResponseEntity<TeamResponse> response = controller.getTeam(1);
        Assertions.assertEquals("team2", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void shall_test_get_all_team_data() {

        Team team = Team.builder().teamCount(0).teamMembers(List.of()).managerEmpId("we123").name("team2").build();
        Mockito.when(service.getAllTeam()).thenReturn(List.of(team));
        ResponseEntity<List<Team>> response = controller.getAllTeam();
        Assertions.assertEquals("team2", Objects.requireNonNull(response.getBody()).get(0).getName());
    }

    @Test
    void shall_test_get_team_name() {

        TeamData team = TeamData.builder().name("team3").managerId("we123").build();
        Mockito.when(service.getAllTeamName()).thenReturn(List.of(team));
        ResponseEntity<List<TeamData>> response = controller.getTeamName();
        Assertions.assertEquals("team3", Objects.requireNonNull(response.getBody()).get(0).getName());
    }
}

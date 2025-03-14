package com.employee.service;

import com.employee.entity.dto.TeamResponseDto;
import com.employee.entity.request.TeamRequest;
import com.employee.entity.response.TeamData;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Team;
import com.employee.repository.TeamRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService service;

    @Mock
    private TeamRepo repo;

    @Test
    void shall_save_team_details() {
        Team team = Team.builder().name("team 1").teamCount(0).teamMembers(List.of()).build();

        when(repo.save(team)).thenReturn(team);
        service.saveTeamDetails("team 1");

        verify(repo, times(1)).save(team);
    }

    @Test
    void shall_save_team_details_throws_exception() {
        Team team = Team.builder().name("").teamCount(0).teamMembers(List.of()).build();
        EmployeeExceptions exceptions = assertThrows(EmployeeExceptions.class, () -> service.saveTeamDetails(""));

        assertEquals("Team Name Invalid", exceptions.getMessage());
        verify(repo, times(0)).save(team);
    }

    @Test
    void shall_save_same_team_details_throws_exception() {
        Team team = Team.builder().name("team1").teamCount(0).teamMembers(List.of()).build();

        when(repo.findByName("team1")).thenReturn(team);
        EmployeeExceptions exceptions = assertThrows(EmployeeExceptions.class, () -> service.saveTeamDetails("team1"));

        assertEquals("Team Already Created", exceptions.getMessage());
        verify(repo, times(0)).save(team);
    }

    @Test
    void shall_get_team_data_by_id() {

        int teamId = 1;
        TeamResponseDto responseDto = TeamResponseDto.builder().name("team1").teamCount(0).managerName("").managerEmpId("").build();

        when(repo.findTeamById(teamId)).thenReturn(responseDto);

        service.getTeamData(teamId);

        verify(repo, times(1)).findTeamById(teamId);

    }

    @Test
    void shall_get_team_data_by_id_throw_exception() {

        int teamId = 1;
        when(repo.findTeamById(teamId)).thenThrow(EmployeeExceptions.class);

        EmployeeExceptions exceptions = assertThrows(EmployeeExceptions.class, () -> service.getTeamData(teamId));

        assertEquals("No Data Found For Team Id " + teamId, exceptions.getMessage());
        verify(repo, times(1)).findTeamById(teamId);
    }

    @Test
    void shall_get_team_name_manager() {
        Team team = Team.builder().name("team1").teamCount(0).teamMembers(List.of()).managerEmpId("we123").build();

        when(repo.findAll()).thenReturn(List.of(team));

        List<TeamData> teamData = service.getAllTeamName();

        assertEquals(teamData.get(0).getManagerId(), team.getManagerEmpId());
        verify(repo, times(1)).findAll();
    }


    @Test
    void shall_get_team_name_manager_null() {

        when(repo.findAll()).thenReturn(List.of());

        List<TeamData> teamData = service.getAllTeamName();

        assertEquals(0, teamData.size());
        verify(repo, times(1)).findAll();
    }


    @Test
    void shall_get_all_team_details() {
        Team team = Team.builder().name("team1").teamCount(0).teamMembers(List.of()).managerEmpId("we123").build();

        when(repo.findAll()).thenReturn(List.of(team));

        List<Team> teamData = service.getAllTeam();

        assertEquals(1, teamData.size());
        verify(repo, times(1)).findAll();
    }


    @Test
    void shall_get_all_team_details_null() {
        when(repo.findAll()).thenReturn(List.of());

        List<Team> teamData = service.getAllTeam();

        assertEquals(0, teamData.size());
        verify(repo, times(1)).findAll();
    }

}

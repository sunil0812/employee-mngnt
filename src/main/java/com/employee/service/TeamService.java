package com.employee.service;

import com.employee.entity.dto.TeamResponseDto;
import com.employee.entity.request.TeamRequest;
import com.employee.entity.response.TeamData;
import com.employee.entity.response.TeamResponse;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Team;
import com.employee.repository.TeamRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TeamService {

    @Autowired
    private TeamRepo repo;

    public TeamResponse getTeamData(int id) {
        try {
            TeamResponseDto responseDto = repo.findTeamById(id);
            return TeamResponse.builder().id(responseDto.getId()).name(responseDto.getName()).managerName(responseDto.getManagerName()).teamCount(responseDto.getTeamCount()).managerEmpId(responseDto.getManagerEmpId()).members(responseDto.getMembers()).build();
        } catch (Exception e) {
            throw new EmployeeExceptions("No Data Found For Team Id " + id);
        }
    }

    public Team saveTeamDetails(TeamRequest team) {
        Team team1 = repo.findByName(team.getName().toLowerCase());
        if (team.getName().replace(" ","").isEmpty()){
            throw new EmployeeExceptions("Team Name not provided");
        }else if (team1 != null){
            throw new EmployeeExceptions("Team Already Created");
        }
        Team saveTeam = Team.builder()
                .name(team.getName().toLowerCase())
                .managerEmpId(null)
                .teamMembers(List.of())
                .teamCount(0)
                .build();

        repo.save(saveTeam);
        log.info("New Team Added Name{} - Members{} - ManagerId{} ", team.getName(), team.getTeamMembers(), team.getManagerEmpId());
        return saveTeam;
    }

    public List<TeamData> getAllTeamName() {
        List<Team> teams  = repo.findAll();
        List<TeamData> list = new ArrayList<>();
        teams.forEach(teamData -> {
            TeamData data = TeamData.builder().name(teamData.getName()).managerId(teamData.getManagerEmpId()).build();
            list.add(data);
        });
        return list;
    }

    public List<Team> getAllTeam() {
       return repo.findAll();
    }
}

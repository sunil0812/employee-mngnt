package com.employee.controller;

import com.employee.entity.request.TeamRequest;
import com.employee.entity.response.TeamData;
import com.employee.entity.response.TeamResponse;
import com.employee.model.Team;
import com.employee.service.EmployeeService;
import com.employee.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService service;


    @GetMapping("/getTeam/{id}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable int id) {
        return ResponseEntity.ok(service.getTeamData(id));
    }

    @GetMapping("/getTeam")
    public ResponseEntity<List<Team>> getAllTeam() {
        return ResponseEntity.ok(service.getAllTeam());
    }

    @PostMapping("/saveTeam")
    public ResponseEntity<Team> saveTeam(@RequestBody TeamRequest team) {
        return ResponseEntity.ok(service.saveTeamDetails(team));
    }

    @GetMapping("/teamName")
    public ResponseEntity<List<TeamData>> getTeamName() {
        return ResponseEntity.ok(service.getAllTeamName());
    }

}

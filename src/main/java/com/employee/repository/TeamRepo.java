package com.employee.repository;

import com.employee.entity.dto.TeamResponseDto;
import com.employee.model.Team;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TeamRepo extends JpaRepository<Team,Long> {

    @Query(value = "select * from employee_message.team_data where name = :teamName",nativeQuery = true)
    Team findByName(@Param("teamName") String teamName);
    @Query(value = "select exists(select 1 from employee_message.team_data where name=:teamName)",nativeQuery = true)
    boolean existsByTeamName(@Param("teamName")String teamName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE employee_message.team_data SET manager_emp_id = :empId WHERE name= :name",nativeQuery = true)
    void updateManagerEmpId(@Param("empId") String empId,@Param("name") String name);

    @Query("SELECT new com.employee.entity.dto.TeamResponseDto(t.id, t.name, t.teamMembers, t.managerEmpId, t.teamCount, m.name) FROM Team t LEFT JOIN Manager m ON t.managerEmpId = m.empId WHERE t.id= CAST(?1 as integer)")
    TeamResponseDto findTeamById(int id);

    @Query(value = "SELECT * FROM employee_message.team_data where manager_emp_id=:empId",nativeQuery = true)
    Team findByManagerEmpId(@Param("empId") String empId);

    @Query(value = "SELECT * FROM employee_message.team_data where deleted=false",nativeQuery = true)
    List<Team> findAll();
}

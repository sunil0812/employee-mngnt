package com.employee.service;

import com.employee.configuration.StatusConfiguration;
import com.employee.constants.EmployeeConstants;
import com.employee.entity.request.ChangePassword;
import com.employee.entity.request.EmployeeEntity;
import com.employee.entity.dto.EmployeeResponseDto;
import com.employee.entity.request.LogInRequest;
import com.employee.entity.response.BaseEmployeeResponse;
import com.employee.entity.response.EmployeeResponse;
import com.employee.entity.response.ManagerData;
import com.employee.entity.response.ManagerResponse;
import com.employee.entity.response.TeamData;
import com.employee.entity.response.UpdateResponse;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.Address;
import com.employee.model.BankDetails;
import com.employee.model.CompanyDetails;
import com.employee.model.Employee;
import com.employee.model.EmployeeNameTracking;
import com.employee.model.Manager;
import com.employee.model.Team;
import com.employee.repository.CompanyDetailsRepo;
import com.employee.repository.EmployeeNameTrackingRepo;
import com.employee.repository.EmployeeRepo;
import com.employee.repository.ManagerRepo;
import com.employee.repository.TeamRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.employee.constants.EmployeeConstants.validateRole;
import static com.employee.model.Employee.empIdByName;
import static com.employee.model.Employee.empIdFirstName;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepo repo;

    private final TeamRepo teamRepo;

    private final ManagerRepo managerRepo;

    private final ObjectMapper mapper;

    private final EmployeeNameTrackingRepo nameRepo;

    private final StatusConfiguration values;

    private final CompanyDetailsRepo companyRepo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public EmployeeService(StatusConfiguration values, EmployeeRepo repo, TeamRepo teamRepo, ManagerRepo managerRepo, ObjectMapper mapper, EmployeeNameTrackingRepo nameRepo, CompanyDetailsRepo companyRepo) {
        this.values = values;
        this.nameRepo = nameRepo;
        this.mapper = mapper;
        this.managerRepo = managerRepo;
        this.teamRepo = teamRepo;
        this.repo = repo;
        this.companyRepo = companyRepo;
    }

    @Transactional
    public Employee saveEmp(EmployeeEntity emp) {
        log.info("New Employee For Role - {}", emp.getRole());
        return Objects.equals(emp.getRole().toUpperCase(), EmployeeConstants.MANAGER.name()) ? saveManagerData(emp) : saveAllEmp(emp);
    }

    private Employee saveManagerData(EmployeeEntity emp) {
        Employee employee = new Employee();
        validatePhoneEmail(emp);
        employee.setEmpId(getEmpId(emp.getName()));
        String empId = employee.getEmpId();
        employee.setName(emp.getName());
        employee.setEmail(emp.getEmail());
        employee.setEmpType(emp.getEmpType());
        employee.setPassword(encoder.encode(employee.getEmpId()));
        employee.setPhone(emp.getPhone());
        employee.setRole(validateRole(emp.getRole()).toString());
        employee.setBankDetails(convertToJson(emp.getBankDetails()));
        employee.setGender(emp.getGender());
        employee.setAddress(convertToJson(emp.getAddress()));
        employee.setDob(emp.getDob());
        if (emp.getTeamName() != null) {
            Team exists = teamRepo.findByName(emp.getTeamName());
            if (exists == null) {
                saveTeamData(emp, employee, empId);
            } else {
                employee.setTeamId(exists);
                log.info("Updating Manager ID {} for Team {}", employee.getEmpId(), emp.getTeamName());
                teamRepo.updateManagerEmpId(employee.getEmpId(), emp.getTeamName());
            }
        }
        //  add company details
        appendCompanyDetails(emp,employee);
        repo.save(employee);
        log.info("New Manager Info Added - Name {} Role {} ", emp.getName(), emp.getRole());
        saveManagerDataDB(emp, empId);
        return employee;
    }

    private void appendCompanyDetails(EmployeeEntity emp, Employee employee) {
        CompanyDetails value = companyRepo.getByCompanyId(emp.getCompanyDetails());
        employee.setCompanyDetails(value);
    }

    private void saveManagerDataDB(EmployeeEntity emp, String empId) {
        Manager manager = new Manager();
        manager.setName(emp.getName());
        manager.setEmpId(empId);
        manager.setTeamName(emp.getTeamName());
        managerRepo.save(manager);
        log.info("Manager data saved - {}", manager);
    }

    private void saveTeamData(EmployeeEntity emp, Employee employee, String empId) {
        Team data = new Team();
        data.setTeamMembers(new ArrayList<>());
        data.setName(emp.getTeamName());
        data.setManagerEmpId(empId);
        data.setTeamCount(0);
        teamRepo.save(data);
        employee.setTeamId(data);
        log.info("New Team data added - {}", data);
    }

    private Employee saveAllEmp(EmployeeEntity emp) {
        try {
            validatePhoneEmail(emp);
            Employee employee = new Employee();
            employee.setEmpId(getEmpId(emp.getName()));
            employee.setPassword(encoder.encode(employee.getEmpId()));
            employee.setName(emp.getName());
            employee.setEmail(emp.getEmail());
            employee.setEmpType(emp.getEmpType());
            employee.setPhone(emp.getPhone());
            employee.setRole(validateRole(emp.getRole()).toString());
            employee.setBankDetails(convertToJson(emp.getBankDetails()));
            employee.setGender(emp.getGender());
            employee.setAddress(convertToJson(emp.getAddress()));
            employee.setDob(emp.getDob());
            if (validateTeamAndManager(emp)) {
                Team team = teamRepo.findByName(emp.getTeamName());
                List<String> members = team.getTeamMembers();
                members.add(employee.getEmpId());

                team.setTeamCount(members.size());
                team.setTeamMembers(members);
                employee.setTeamId(team);
//                add company details
                appendCompanyDetails(emp,employee);
                repo.save(employee);
                teamRepo.save(team);
                log.info("New Employee Info Added - Name {} Role {}", employee.getName(), employee.getRole());
                return employee;
            }
            log.error("Manager {} or Team {} data not found", emp.getManagerEmpId(), emp.getTeamName());
            throw new EmployeeExceptions("Manager or Team Data not found");
        } catch (Exception e) {
            throw new EmployeeExceptions(e.getMessage());
        }
    }

    private void validatePhoneEmail(EmployeeEntity emp) {
        boolean existPhone = repo.existsByPhone(emp.getPhone());
        boolean existMail = repo.existsByEmail(emp.getEmail());
        if (existPhone || existMail) throw new EmployeeExceptions("Mail or Phone already Existed");
    }



    private String convertToJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new EmployeeExceptions(e.getMessage());
        }
    }

    private boolean validateTeamAndManager(EmployeeEntity emp) {
        boolean isValidated = false;
        Optional<Manager> manager = managerRepo.findByEmpId(emp.getManagerEmpId());
        if (manager.isPresent()) {
            Team team = manager.stream()
                    .map(t -> teamRepo.findByName(emp.getTeamName()))
                    .filter(e -> e != null && e.getName().contains(manager.get().getTeamName()))
                    .findFirst()
                    .orElseThrow(() -> new EmployeeExceptions("Team Data Not Found"));
            if (team != null) {
                isValidated = true;
                log.info("team and manager data are validated");
            }
        }

        return isValidated;
    }

    private String getEmpId(String name) {

//        Add logic for taking the hash code from initial
        String[] listName = name.split(" ");
        String firstName = listName[0].length() >= 2 ? listName[0] : listName[1];
        String initial = listName[0].length() > 3 ? listName[1] : listName[0];
        String n = nameRepo.findByFirstName(firstName);
        int value = n == null ? createNameInDb(firstName) : updateValue(Integer.parseInt(n), firstName);
        int index = firstName.length() > 5 ? 6 : firstName.length() - 1;

        return listName.length <= 2 ? empIdFirstName(firstName, initial, value) : empIdByName(firstName, index, initial, value);
    }

    private int updateValue(int count, String firstName) {
        int value = count + 1;
        nameRepo.updateNameCount(value, firstName);
        return value;
    }

    private int createNameInDb(String firstName) {
        nameRepo.save(EmployeeNameTracking.builder().firstName(firstName).nameCount(1).build());
        return 0;
    }

    public BaseEmployeeResponse getEmpResponse(String empId) {
        Employee employee = repo.findByEmpId(empId).orElseThrow(() -> new EmployeeExceptions("Employee Id Not Found: " + empId));
        return employee.getRole().equalsIgnoreCase(EmployeeConstants.MANAGER.name()) ? getManagerData(employee) : getEmpData(employee);
    }

    private BaseEmployeeResponse getEmpData(Employee employee) {
        try {
            log.info("Getting Employee Info for EmpId - [{}], Role - [{}]", employee.getEmpId(), employee.getRole().toUpperCase());
            EmployeeResponseDto responseDto = repo.findEmployeeByEmpId(employee.getEmpId());
            ManagerData data = ManagerData.builder().empId(responseDto.getManagerEmpId()).name(responseDto.getManagerName()).build();
            EmployeeResponse response = EmployeeResponse.builder()
                    .name(responseDto.getName())
                    .email(responseDto.getEmail())
                    .phone(responseDto.getPhone())
                    .gender(responseDto.getGender().trim())
                    .teamName(responseDto.getTeamName())
                    .empId(responseDto.getEmpId())
                    .role(responseDto.getRole())
                    .address(mapper.readValue(responseDto.getAddress(), Address.class))
                    .bankDetails(mapper.readValue(responseDto.getBankDetails(), BankDetails.class))
                    .manager(data)
                    .build();

            return BaseEmployeeResponse.builder().employeeResponse(response).build();
        } catch (JsonProcessingException e) {
            throw new EmployeeExceptions("Json Parsing error");
        }

    }

    private BaseEmployeeResponse getManagerData(Employee employee) {
        try {
            log.info("Getting Employee Info for EmpId - [{}], Role - [{}]", employee.getEmpId(), employee.getRole().toUpperCase());
            EmployeeResponseDto responseDto = managerRepo.findManagerByEmpId(employee.getEmpId());
            TeamData team = TeamData.builder().name(responseDto.getTeamName()).members(responseDto.getTeamMembers()).build();
            ManagerResponse response = ManagerResponse.builder()
                    .name(responseDto.getName())
                    .empId(responseDto.getEmpId())
                    .email(responseDto.getEmail())
                    .role(responseDto.getRole())
                    .gender(responseDto.getGender())
                    .address(mapper.readValue(responseDto.getAddress(), Address.class))
                    .bankDetails(mapper.readValue(responseDto.getBankDetails(), BankDetails.class))
                    .phone(responseDto.getPhone())
                    .teamData(team)
                    .build();
            return BaseEmployeeResponse.builder().managerResponse(response).build();
        } catch (JsonProcessingException e) {
            throw new EmployeeExceptions("Json Parsing error");
        }
    }

    @Transactional
    public UpdateResponse updateDetails(String empId, EmployeeEntity currentValue) {
        try {
            EmployeeEntity existingValue = getExistingEmpValue(empId);
            validateRole(currentValue.getRole());
            if (!Objects.equals(existingValue.toString(), currentValue.toString())) {
                log.info(" Updating the Employee value for [{}] ", empId);
                return saveEmployeeValue(currentValue, existingValue, empId);
            }
            return UpdateResponse.builder().status("Skipped").message("No changes so skipped").build();
        } catch (Exception e) {
            throw new EmployeeExceptions(" Something went wrong: " + e.getMessage());
        }
    }

    public String updateStatus(String empId, String status) {

        if (!status.isEmpty() && !values.getStatusValues().contains(status.toLowerCase()))
            throw new EmployeeExceptions("Given status is not valid: " + status);
        Employee emp = repo.findByEmpId(empId).orElseThrow(() -> new EmployeeExceptions("Employee not found EmpId: " + empId));

        return Objects.equals(status, "remove") ? inActiveEmp(emp) : deleteEmp(emp);
    }

    private String deleteEmp(Employee emp) {
        if (emp.isActive() || emp.getTeamId() != null) {
            throw new EmployeeExceptions("Employee can't delete EmpId: " + emp.getEmpId());
        }
        emp.setDeleted(true);
        repo.save(emp);
        return " Employee Successfully Deleted Employee-Id: " + emp.getEmpId();
    }

    private String inActiveEmp(Employee emp) {
        //        Remove Members from Team
        removeFromTeam(emp);
        emp.setTeamId(null);
        emp.setActive(false);
        repo.save(emp);
        return " Employee Successfully Removed From Team EmpId: " + emp.getEmpId();
    }

    private void removeFromTeam(Employee emp) {
        if (emp.getTeamId() == null) {
            throw new EmployeeExceptions("Employee Already InActive EmpId: " + emp.getEmpId());
        }

        Team team = teamRepo.findById(emp.getTeamId().getId()).orElseThrow(() -> new EmployeeExceptions("Team not found " + emp.getTeamId().getId()));

        if (!Objects.equals(emp.getRole(), EmployeeConstants.MANAGER.name().toUpperCase())) {
            List<String> members = new ArrayList<>(team.getTeamMembers());
//                REMOVING MEMBER FROM TEAM
            members.removeIf(i -> i != null && i.equalsIgnoreCase(emp.getEmpId()));
            team.setTeamMembers(members);
            team.setTeamCount(members.size());
            teamRepo.save(team);
        } else {
            team.setManagerEmpId(null);
            teamRepo.save(team);
        }
    }

    private UpdateResponse saveEmployeeValue(EmployeeEntity currentValue, EmployeeEntity existingValue, String empId) {
        Team teamValue = teamRepo.findById(currentValue.getTeamId()).orElseThrow(() -> new EmployeeExceptions("Team data not found for given ID: " + currentValue.getTeamId()));
        return currentValue.getRole().equalsIgnoreCase("manager") ? updateManagerValue(teamValue, existingValue, currentValue, empId) : updateEmployeeValue(teamValue, existingValue, currentValue, empId);
    }

    private UpdateResponse updateEmployeeValue(Team teamValue, EmployeeEntity existingValue, EmployeeEntity currentValue, String empId) {
        Employee employee = new Employee();

        if (existingValue.getTeamId() == null) {
            ArrayList<String> members = new ArrayList<>(teamValue.getTeamMembers());
            members.add(empId);

            teamValue.setTeamMembers(members);
            teamValue.setTeamCount(members.size());
            teamRepo.save(teamValue);
            log.info("Employee is added to a team - {}", teamValue.getName());
        }

        employee.setTeamId(teamValue);
        employee.setName(currentValue.getName());
        employee.setEmail(currentValue.getEmail());
        employee.setEmpType(currentValue.getEmpType());
        employee.setRole(currentValue.getRole());
        employee.setPhone(currentValue.getPhone());
        employee.setDob(currentValue.getDob());
        employee.setGender(currentValue.getGender());
        employee.setAddress(convertToJson(currentValue.getAddress()));
        employee.setBankDetails(convertToJson(currentValue.getBankDetails()));
        employee.setEmpId(empId);
        repo.save(employee);
        return UpdateResponse.builder().status("Updated").message("Employee details updated for EmpID: " + empId).build();
    }

    private UpdateResponse updateManagerValue(Team teamValue, EmployeeEntity existingValue, EmployeeEntity currentValue, String empId) {
        Employee employee = new Employee(currentValue.getName(), currentValue.getRole(), currentValue.getGender(), currentValue.getEmail(), currentValue.getPhone(), currentValue.getEmpType(), currentValue.getDob());
        Team exisitingTeam = teamRepo.findByManagerEmpId(empId);

        employee.setTeamId(exisitingTeam);
        //        Updating team data for manager
        if (!currentValue.getTeamId().equals(existingValue.getTeamId())) {
            if (teamValue.getManagerEmpId() != null) {
                throw new EmployeeExceptions("Team have existing manager data " + exisitingTeam.getManagerEmpId());
            }
            employee.setTeamId(teamValue);
            teamValue.setManagerEmpId(empId);
            teamRepo.save(teamValue);
//            Remove Manager ID from Existing Team
            exisitingTeam.setManagerEmpId(null);
            teamRepo.save(exisitingTeam);
        }
        employee.setEmpId(empId);
        employee.setAddress(convertToJson(currentValue.getAddress()));
        employee.setBankDetails(convertToJson(currentValue.getBankDetails()));
        repo.save(employee);
        return UpdateResponse.builder().status("Updated").message("Employee details updated for EmpID: " + empId).build();
    }

    private EmployeeEntity getExistingEmpValue(String empId) throws JsonProcessingException {
        Employee emp = repo.findByEmpId(empId).orElseThrow(() -> new EmployeeExceptions("No result found for this id :: " + empId));

        return EmployeeEntity.builder().name(emp.getName())
                .role(emp.getRole().toLowerCase())
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .gender(emp.getGender())
                .dob(emp.getDob())
                .empType(emp.getEmpType())
                .teamName(emp.getTeamId() == null ? null : emp.getTeamId().getName())
                .teamId(emp.getTeamId() == null ? null : emp.getTeamId().getId())
                .address(mapper.readValue(emp.getAddress(), Address.class))
                .bankDetails(mapper.readValue(emp.getBankDetails(), BankDetails.class))
                .build();
    }

    public List<Employee> getAllEmpResponse(String empId) {
       ArrayList<Employee> employeeList = new ArrayList<>();
        Employee employee = repo.findByEmpId(empId).orElseThrow(() -> new EmployeeExceptions("USER NOT FOUND"));
        if (employee.getRole().equals("MANAGER")){
           Team team = teamRepo.findByManagerEmpId(empId);
           team.getTeamMembers().forEach(a -> {
                  Employee emp = repo.findByEmpId(a).orElseThrow(() -> new EmployeeExceptions("USER NOT FOUND: "+a));
                 employeeList.add(emp);
           });
           return employeeList;
        }
        List<Employee> emp = repo.findAll();
        if (emp == null || emp.isEmpty()) throw new EmployeeExceptions("No Data found");
//        emp.stream().filter(employee -> employee.getUpdatedAt() != null && employee.getCreatedAt() != null).forEach(EmployeeService::formatTimestamp);
        return emp;
    }

    public static void formatTimestamp(Employee emp) {
        LocalDateTime updated = LocalDateTime.parse(String.valueOf(emp.getUpdatedAt()), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));

        LocalDateTime created = LocalDateTime.parse(emp.getCreatedAt().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));

        // Format it into a more readable format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        emp.setUpdatedAt(Timestamp.valueOf(updated.format(formatter)));
        emp.setCreatedAt(Timestamp.valueOf(created.format(formatter)));

    }

    public String validateCredentials(LogInRequest request) {

        Employee employee = repo.findByEmpId(request.getUserName()).orElseThrow(() -> new EmployeeExceptions("User Not Found: " + request.getUserName()));
        boolean isPasswordMatch = encoder.matches(request.getPassword(),employee.getPassword());
        if (!isPasswordMatch) {
            System.out.println("inside password not match");
            return "Invalid Password";
        } else if (employee.getEmpId().contains(request.getPassword())) {
            System.out.println("inside change password");
            return "Change password";
        }
        System.out.println("inside User credentials validated");

        return "User credentials validated";
    }

    public void changePassword(String empId, ChangePassword request) {
       Employee emp = repo.findByEmpId(empId).orElseThrow(() -> new EmployeeExceptions("USER NOT FOUND: "+empId));
        if (request!=null && request.getConfirmPassword().equals(request.getNewPassword())){
            emp.setPassword(encoder.encode(request.getConfirmPassword()));
            repo.save(emp);
            System.out.println("password updated");

        }
        System.out.println("password not match");
    }
}

package com.employee.service;

import com.employee.model.Employee;
import com.employee.model.UserPrincipal;
import com.employee.repository.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> emp = employeeRepo.findByEmpId(username);
        if (!emp.isPresent()){
            throw new UsernameNotFoundException("EMPLOYEE NOT FOUND: "+username);
        }
        return new UserPrincipal(emp.get());
    }
}

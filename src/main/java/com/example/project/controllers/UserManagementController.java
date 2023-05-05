package com.example.project.controllers;

import com.example.project.model.Employee;
import com.example.project.model.Roles;
import com.example.project.repository.EmployeeRepository;
import com.example.project.security.RoleEnum;
import com.example.project.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController(value = "userManagement")
@RequiredArgsConstructor
public class UserManagementController {

    private final EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    @PostMapping("/addNewEmployee") // todo only admins can do that
    public void addNewEmployee(@RequestBody Employee employee) {
        employeeService.addNewEmployee(employee);
    }

    @PutMapping("/modifyRoles/{id}")
    public void modifyRoles(@RequestBody RoleEnum roleEnum) {

    }

    @GetMapping("/getRoles/{username}")
    public Set<Roles> getRoles(@PathVariable String username) {
        return employeeService.getRolesFromEmployee(username);
    }

    @GetMapping("/getEmployees/{roleName}")
    public Set<Roles> getEmployees(@PathVariable String roleName) {
        return employeeService.getRolesFromEmployee(roleName);
    }
}
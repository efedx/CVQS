package com.example.project.controllers;

import com.example.project.model.Employee;
import com.example.project.security.RoleEnum;
import com.example.project.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "personnelManagement")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/addNewEmployee") // todo only admins can do that
    public void addNewEmployee(@RequestBody Employee employee) {
        employeeService.addNewEmployee(employee);
    }

    @PutMapping("/modifyRoles/{id}")
    public void modifyRoles(@RequestBody RoleEnum roleEnum) {

    }

}

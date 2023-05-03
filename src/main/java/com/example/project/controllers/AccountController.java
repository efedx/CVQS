package com.example.project.controllers;

import com.example.project.model.Employee;
import com.example.project.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    AccountController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("nonDeletedEmployees")
    public List<Employee> getNonDeletedEmployees() {
        return employeeService.getNonDeletedEmployees();
    }

    @GetMapping("deletedEmployees")
    public List<Employee> getDeletedEmployees() {
        return employeeService.getDeletedEmployees();
    }

//    @PostMapping("register") todo deal with that
//    public void registerNewEmployee(@RequestBody Employee employee) {
//        employeeService.registerNewEmployee(employee);
//        System.out.println(employee);    }

    @DeleteMapping(path="{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("{id}")
    public void updateEmployee(@PathVariable Long id,
                               @RequestParam(name = "username", required = false) String username,
                               @RequestParam(name = "email", required = false) String email) {
        employeeService.updateEmployee(id, username, email);
    }
}

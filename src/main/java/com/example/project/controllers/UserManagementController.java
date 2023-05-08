package com.example.project.controllers;

import com.example.project.dto.AuthenticationResponseDto;
import com.example.project.dto.RegisterRequestDto;
import com.example.project.dto.UpdateRequestDto;
import com.example.project.model.Roles;
import com.example.project.repository.EmployeeRepository;
import com.example.project.security.RoleEnum;
import com.example.project.services.EmployeeService;
import com.example.project.services.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserManagementController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserManagementService userManagementService;

    //------------------------------------------------------

    @Validated
    @PostMapping("/userManagement/registerEmployee") // todo only admins can do that
    public ResponseEntity<AuthenticationResponseDto> registerNewEmployee(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(userManagementService.registerNewEmployee(registerRequestDto));
    }

    @PostMapping("/userManagement/deleteEmployeeById/{id}")
    public String deleteEmployeeById(@PathVariable Long id) {
        return userManagementService.deleteEmployeeById(id);
    }

    @PutMapping("/userManagement/updateEmployeeById/{id}")
    public String updateEmployeeById(@PathVariable Long id, @RequestBody UpdateRequestDto updateRequestDto) {
        return userManagementService.updateEmployee(id, updateRequestDto);
    }




    @PutMapping("/userManagement/modifyRoles/{id}")
    public void modifyRoles(@RequestBody RoleEnum roleEnum) {
    }

    @GetMapping("/userManagement/getRoles/{id}")
    public Set<Roles> getRoles(@PathVariable Long id) {
        return employeeService.getRolesFromEmployee(id);
    }

//    @GetMapping("/userManagement/getEmployees/{roleName}")
//    public Set<Roles> getEmployees(@PathVariable String roleName) {
//        return employeeService.getRolesFromEmployee(roleName);
//    }
}
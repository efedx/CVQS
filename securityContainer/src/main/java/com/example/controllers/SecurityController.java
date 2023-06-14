package com.example.controllers;

import com.example.dto.JwtDto;
import com.example.dto.LoginRequestDto;
import com.example.dto.RegisterRequestDto;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.services.JwtValidaitonService;
import com.example.services.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    @Autowired
    UserManagementService userManagementService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JwtValidaitonService jwtValidaitonService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Validated
    @PostMapping("/registerAdmin") // todo only admins can do that
    public ResponseEntity<String> registerAdmin(@RequestBody List<RegisterRequestDto> registerRequestDtoList) {

        for(RegisterRequestDto registerRequestDto: registerRequestDtoList) {

            Optional<Employee> employeeControl = employeeRepository.findByUsername(registerRequestDto.getUsername());

            // check if the user with that username exists
            if(employeeControl.isPresent()) {
                throw new IllegalStateException("Username taken");
            }

            // if not create an employee
            String  username = registerRequestDto.getUsername();

            Employee employee = new Employee();
            employee.setUsername(username);
            employee.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            employee.setEmail(registerRequestDto.getEmail());
            employee.setRoles(getRolesSetFromRoleDtoSet(employee, registerRequestDto.getRoleSet()));

            employeeRepository.save(employee);
        }
        return ResponseEntity.status(HttpStatus.OK).body("test");
    }

    private Set<Roles> getRolesSetFromRoleDtoSet(Employee employee, Set<RegisterRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();

        for (RegisterRequestDto.RoleDto roleDto : roleDtoSet) {

            Roles role = new Roles(employee, roleDto.getRoleName());
            newRolesSet.add(role);
        }
        return newRolesSet;
    }

    @PostMapping("/login") // todo only admins can do that
    public ResponseEntity<JwtDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(JwtDto.builder().username(loginRequestDto.getUsername()).token(userManagementService.login(loginRequestDto)).build());
    }

    @PostMapping("/userManagement") // todo only admins can do that /registerEmployee
    public ResponseEntity<Boolean> userManagement(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidaitonService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/defects") // todo only admins can do that /registerEmployee
    public ResponseEntity<Boolean> defects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidaitonService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/listDefects") // todo only admins can do that /registerEmployee
    public ResponseEntity<Boolean> listDefects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidaitonService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/terminals") // todo only admins can do that /registerEmployee
    public ResponseEntity<Boolean> terminals(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidaitonService.isTokenValid(authorizationHeader));
    }
}

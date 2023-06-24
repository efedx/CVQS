package com.example.controllers;

import com.example.dto.JwtDto;
import com.example.dto.LoginRequestDto;
import com.example.dto.RegisterRequestDto;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.services.JwtValidationService;
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
    JwtValidationService jwtValidationService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login") // NO AUTHORIZATION
    public ResponseEntity<JwtDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(JwtDto.builder().username(loginRequestDto.getUsername()).token(userManagementService.login(loginRequestDto)).build());
    }

    @PostMapping("/userManagement") // ADMIN
    public ResponseEntity<Boolean> userManagement(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/defects") // OPERATOR
    public ResponseEntity<Boolean> defects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/listDefects") // LEADER
    public ResponseEntity<Boolean> listDefects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/terminals") // ALL
    public ResponseEntity<Boolean> terminals(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }
}

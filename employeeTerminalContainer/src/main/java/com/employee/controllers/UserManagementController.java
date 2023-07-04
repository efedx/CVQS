package com.employee.controllers;

import com.common.*;
import com.employee.dto.UpdateRequestDto;
import com.employee.exceptions.TakenUserNameException;
import com.employee.repository.EmployeeRepository;
import com.employee.services.UserManagementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final EmployeeRepository employeeRepository;

    //-----------------------------------------------------------------------------------------------

    @GetMapping("/test1")
    public String test1() {
        int id = 5;
        throw new TakenUserNameException("Id " + 5 + " taken");
    }
    @GetMapping("/test2")
    public String test2() {
        return "test";
    }


    @Validated
    @PostMapping("/registerAdmin") // todo only admins can do that
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerAdmin(@RequestBody List<@Valid RegisterRequestDto> registerRequestDtoList) {

        Set<Employee> employeeSet = userManagementService.registerAdmin(registerRequestDtoList);
        return ResponseEntity.ok("Employees saved");
    }

    @Validated
    @PostMapping("/userManagement/registerEmployee") // todo only admins can do that
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerNewEmployee(@RequestHeader("Authorization") String authorizationHeader,
                                                      @Valid @RequestBody List<RegisterRequestDto> registerRequestDtoList) throws JsonProcessingException {
        userManagementService.registerEmployee(authorizationHeader, registerRequestDtoList);
        return ResponseEntity.ok("Employees saved");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        JwtDto jwtDto = userManagementService.login(loginRequestDto);
        String username = jwtDto.getUsername();
        String token = jwtDto.getToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);

        return ResponseEntity.ok().headers(httpHeaders).body("Username with " + username + "'s token is: " + token);
    }

    @PostMapping("/userManagement/deleteEmployeeById/{id}")
    public ResponseEntity<String> deleteEmployeeById(@RequestHeader("Authorization") String authorizationHeader,
                                     @PathVariable Long id) throws JsonProcessingException {

        userManagementService.deleteEmployeeById(authorizationHeader, id);
        return ResponseEntity.ok().body("Id with " + id + " deleted");
    }

    @PutMapping("/userManagement/updateEmployeeById/{id}")
    public ResponseEntity<String> updateEmployeeById(@RequestHeader("Authorization") String authorizationHeader,
                                     @PathVariable Long id,
                                     @RequestBody UpdateRequestDto updateRequestDto) throws JsonProcessingException {
        userManagementService.updateEmployee(authorizationHeader, id, updateRequestDto);
        return ResponseEntity.ok().body("Id with " + id + " updated");
    }

    @GetMapping("/userManagement/{id}")
    public Set<Roles> getRoles(@PathVariable String id) {
        Employee employee = employeeRepository
                .findByUsername(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + id));
        Set<Roles> rolesSet = employee.getRoles();
        return rolesSet;
    }
}
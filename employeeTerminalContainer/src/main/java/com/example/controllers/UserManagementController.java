package com.example.controllers;

import com.example.dto.JwtDto;
import com.example.dto.LoginRequestDto;
import com.example.dto.RegisterRequestDto;
import com.example.dto.UpdateRequestDto;
import com.example.exceptions.TakenUserNameException;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.services.UserManagementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserManagementController {

//    private static final Logger logger = LogManager.getLogger(UserManagementController.class);

    @Autowired
    private UserManagementService userManagementService;
    private final EmployeeRepository employeeRepository;

    //------------------------------------------------------

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
       // logger.info("created");

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

        //String username = employeeRepository.findByUsername("1").get().getUsername();

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + id));;
        Set<Roles> rolesSet = employee.getRoles();
        return rolesSet;
    }




//    @PutMapping("/userManagement/modifyRoles/{id}")
//    public void modifyRoles(@RequestBody RoleEnum roleEnum) {
//    }
//
//    @GetMapping("/userManagement/getRoles/{id}")
//    public Set<Roles> getRoles(@PathVariable Long id) {
//        return employeeService.getRolesFromEmployee(id);
//    }

//    @GetMapping("/userManagement/getEmployees/{roleName}")
//    public Set<Roles> getEmployees(@PathVariable String roleName) {
//        return employeeService.getRolesFromEmployee(roleName);
//    }
}
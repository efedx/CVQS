package com.example.controllers;

import com.example.dto.LoginRequestDto;
import com.example.dto.RegisterRequestDto;
import com.example.dto.UpdateRequestDto;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.services.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;
    private final EmployeeRepository employeeRepository;

    //------------------------------------------------------

    @GetMapping("test1")
    public String test1() {
        return "test";
    }
    @GetMapping("/test2")
    public String test2() {
        return "test";
    }
    @Validated
    @PostMapping("/registerAdmin") // todo only admins can do that
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody List<RegisterRequestDto> registerRequestDtoList) {
        return ResponseEntity.ok(userManagementService.registerAdmin(registerRequestDtoList));
    }

    @Validated
    @PostMapping("/userManagement/registerEmployee") // todo only admins can do that
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerNewEmployee(@Valid @RequestBody List<RegisterRequestDto> registerRequestDtoList,
                                                      @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userManagementService.registerEmployee(registerRequestDtoList, authorizationHeader));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {

        String jwt = userManagementService.login(loginRequestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwt);

        //String username = employeeRepository.findByUsername("1").get().getUsername();

        return ResponseEntity.ok().headers(httpHeaders).body(jwt);
    }

    @PostMapping("/userManagement/deleteEmployeeById/{id}")
    public String deleteEmployeeById(@RequestHeader("Authorization") String jwt,
                                     @PathVariable Long id,
                                     @RequestHeader("Authorization") String authorizationHeader) {
        return userManagementService.deleteEmployeeById(jwt, id, authorizationHeader);
    }

    @PutMapping("/userManagement/updateEmployeeById/{id}")
    public String updateEmployeeById(@RequestHeader("Authorization") String jwt, @PathVariable Long id,
                                     @RequestBody UpdateRequestDto updateRequestDto,
                                     @RequestHeader("Authorization") String authorizationHeader) {
        return userManagementService.updateEmployee(jwt, id, updateRequestDto, authorizationHeader);
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
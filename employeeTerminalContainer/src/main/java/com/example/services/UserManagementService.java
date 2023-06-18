package com.example.services;

import com.example.dto.*;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.repository.RolesRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UserManagementService {

    private static final String securityLoginUrl = "http://security:8083/login";
    private static final String securityUserManagementUrl = "http://security:8083/userManagement";

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //--------------------------------------------------

    public Set<Employee> registerEmployee(String authorizationHeader, List<RegisterRequestDto> registerRequestDtoList) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityUserManagementUrl, HttpMethod.POST, requestEntity, Object.class);

        Set<Employee> employeeSet = new HashSet<>();

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

            employeeSet.add(employeeRepository.save(employee));
        }
        return employeeSet;
    }

    public Set<Employee> registerAdmin(List<RegisterRequestDto> registerRequestDtoList) {

        Set<Employee> employeeSet = new HashSet<>();

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

            employeeSet.add(employeeRepository.save(employee));
        }
        return employeeSet;
    }

    public JwtDto login(LoginRequestDto loginRequestDto) {

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto); // first parameter is the body
        ResponseEntity<JwtDto> jwtResponse = restTemplate.exchange(securityLoginUrl, HttpMethod.POST, requestEntity, JwtDto.class);

        JwtDto jwtDto = jwtResponse.getBody();

        return jwtDto;
    }


    @Transactional
    public Long deleteEmployeeById(String authorizationHeader, Long id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityUserManagementUrl, HttpMethod.POST, requestEntity, Object.class);

        if(!employeeRepository.existsById(id)) throw new IllegalStateException("Employee with id " + id + " does not exists");

        else {
            employeeRepository.setDeletedTrue(id);
            return id;
        }
    }

    @Transactional
    public Employee updateEmployee(String authorizationHeader, Long id, UpdateRequestDto updateRequestDto) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityUserManagementUrl, HttpMethod.POST, requestEntity, Object.class);

        if(!employeeRepository.existsById(id)) throw new IllegalStateException("Employee with id " + id + " does not exists");

        String username = updateRequestDto.getUsername();

        String password = null;
        if(updateRequestDto.getPassword() != null) {
             //password = passwordEncoder.encode(updateRequestDto.getPassword());
             password = updateRequestDto.getPassword();
        }

        String email = updateRequestDto.getEmail();

        //Set<Roles> rolesSet = getRolesSetRoleFromDtoSet(id, updateRequestDto.getRoleSet());

//       employeeRepository.updateEmployeeById(id, username, password, email, rolesSet);

        employeeRepository.updateEmployeeById(id, username, password, email);

        return employeeRepository.findById(id).get();

//        Optional<Employee> employeeOptional = employeeRepository.findById(id);
//        return employeeOptional.orElseGet(employeeOptional::get);

//        getRolesSetRoleFromDtoSet(id, updateRequestDto.getRoleSet());

        //employeeRepository.updateEmployeeRoles(id, rolesSet);
//        Null nul = null;
//        employeeRepository.deleteRoles(id, nul);



//        Employee employee = employeeRepository.findById(id).orElseThrow();
//        rolesRepository.deleteByEmployeeId(id);
//        rolesRepository.saveAll(rolesSet);

//        employee.addRoleSet(rolesSet);
//
//        employeeRepository.deleteById(id);
//        employeeRepository.save(employee);
        //employeeRepository.putRoles(id, rolesSet);

        //employeeRepository.updateEmployeeRoles(id, rolesSet);
    }

    //-----------------------------------------------------

    // create a Set<roles> from the array of String roles received from RegisterRequestDto
    public Set<Roles> getRolesSetFromRoleDtoSet(Employee employee, Set<RegisterRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();

        for (RegisterRequestDto.RoleDto roleDto : roleDtoSet) {

            Roles role = new Roles(employee, roleDto.getRoleName());
            //Roles role = Roles.builder().employee(employee).roleName(roleDto.getRoleName()).build();
            newRolesSet.add(role);
        }
        return newRolesSet;
    }

    private Set<Roles> getRolesSetRoleFromDtoSet(Long id, Set<UpdateRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();
        Employee employee = employeeRepository.findById(id).orElseThrow();
        for (UpdateRequestDto.RoleDto roleDto : roleDtoSet) {

            Roles role = new Roles(employee, roleDto.getRoleName());
            newRolesSet.add(role);
        }
        return newRolesSet;
    }

    private void getRolesSetRoleFromDtoSet2(Long id, Set<UpdateRequestDto.RoleDto> roleDtoSet) {

        Employee employee = employeeRepository.findById(id).orElseThrow();
        employee.getRoles().clear();

//        for (UpdateRequestDto.RoleDto roleDto : roleDtoSet) {
//
//            Roles role = new Roles();
//            role.setEmployee(employee);
//            role.setRoleName(roleDto.getRoleName());
//
//            employee.getRoles().add(role);
//        }
        employeeRepository.save(employee);
    }
}

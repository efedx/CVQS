package com.example.services;

import com.example.dto.*;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import com.example.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementService {

    private static final String securityRegisterEmployeeUrl = "http://security:8083/userManagement/registerEmployee";
    private static final String securityRegisterAdminUrl = "http://security:8083/registerAdmin";
    private static final String securityLoginUrl = "http://security:8083/login";
    private static final String securityJwtValidationUrl = "http://security:8083/isTokenValid";
    private static final String securityUserManagementUrl = "http://security:8083/userManagement";

    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    //--------------------------------------------------

//    public AuthenticationResponseDto registerNewEmployee(List<RegisterRequestDto> registerRequestDtoList) {
//
//        ArrayList<String> jwtList = new ArrayList<>();
//
//        for(RegisterRequestDto registerRequestDto: registerRequestDtoList) {
//
//            Optional<Employee> employeeControl = employeeRepository.findByUsername(registerRequestDto.getUsername());
//
//            // check if the user with that username exists
//            if(employeeControl.isPresent()) {
//                throw new IllegalStateException("Username taken");
//            }
//
//            // if not create an employee
//            String  username = registerRequestDto.getUsername();
//
//            Employee employee = new Employee();
//            employee.setUsername(username);
//            employee.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
//            employee.setEmail(registerRequestDto.getEmail());
//            employee.setRoles(getRolesSetFromRoleDtoSet(employee, registerRequestDto.getRoleSet()));
//
//            employeeRepository.save(employee);
//            log.info("Employee {} is saved", employee.getUsername());
//
//            // create a jwt using the employee and send it with authentication response
//            String jwt = jwtGenerationService.generateJwt(username, employee.getRoles());
//
//            jwtList.add(jwt);
//        }
//        return AuthenticationResponseDto.builder().tokenList(jwtList).build();
//    }

    public String registerEmployee(String authorizationHeader, List<RegisterRequestDto> registerRequestDtoList) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityUserManagementUrl, HttpMethod.POST, requestEntity, Object.class);

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
            log.info("Employee {} is saved", employee.getUsername());
        }
        return "Employees registered";
    }

    public String registerAdmin(List<RegisterRequestDto> registerRequestDtoList) {

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
            log.info("Employee {} is saved", employee.getUsername());
        }
        return "Employees registered";
    }

    public String login(LoginRequestDto loginRequestDto) {

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto); // first parameter is the body
        ResponseEntity<JwtDto> jwtResponse = restTemplate.exchange(securityLoginUrl, HttpMethod.POST, requestEntity, JwtDto.class);

        String jwt = jwtResponse.getBody().getToken();

        return jwt;
    }


    @Transactional
    public String deleteEmployeeById(String authorizationHeader, Long id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityUserManagementUrl, HttpMethod.POST, requestEntity, Object.class);

        if(!employeeRepository.existsById(id)) throw new IllegalStateException(id + " does not exists");

        else {
            employeeRepository.setDeletedTrue(id);
            return "employee deleted";
        }
    }

    @Transactional
    public String updateEmployee(String authorizationHeader, Long id, UpdateRequestDto updateRequestDto) {

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.set("Authorization", authorizationHeader);
//        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

//        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityUserManagementUrl, HttpMethod.POST, requestEntity, Object.class);

        String username = updateRequestDto.getUsername();

        String password = null;
        if(updateRequestDto.getPassword() != null) {
             password = passwordEncoder.encode(updateRequestDto.getPassword());
        }

        String email = updateRequestDto.getEmail();

        //Set<Roles> rolesSet = getRolesSetRoleFromDtoSet(id, updateRequestDto.getRoleSet());

//       employeeRepository.updateEmployeeById(id, username, password, email, rolesSet);

        //employeeRepository.updateEmployeeById(id, username, password, email);

        getRolesSetRoleFromDtoSet(id, updateRequestDto.getRoleSet());

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
        return "employee updated";
    }

    //-----------------------------------------------------

    // create a Set<roles> from the array of String roles received from RegisterRequestDto
    private Set<Roles> getRolesSetFromRoleDtoSet(Employee employee, Set<RegisterRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();

        for (RegisterRequestDto.RoleDto roleDto : roleDtoSet) {

            Roles role = new Roles(employee, roleDto.getRoleName());
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

package com.example.project.services;

import com.example.project.dto.AuthenticationResponseDto;
import com.example.project.dto.LoginRequestDto;
import com.example.project.dto.RegisterRequestDto;
import com.example.project.dto.UpdateRequestDto;
import com.example.project.model.Employee;
import com.example.project.model.Roles;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JwtGenerationService jwtGenerationService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    //--------------------------------------------------

    public AuthenticationResponseDto registerNewEmployee(List<RegisterRequestDto> registerRequestDtoList) {

        ArrayList<String> jwtList = new ArrayList<>();

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
            //employee.setRoles(getRolesEnumFromRoleDtoSetWithEmployee(employee, registerRequestDto.getRoleSet()));

//            Employee employee = Employee.builder()
//                    .username(username)
//                    .password(passwordEncoder.encode(registerRequestDto.getPassword()))
//                    .email(registerRequestDto.getEmail())
//                    .roles(getRolesEnumFromRoleDtoSet(registerRequestDto.getRoleSet()))
//                    .build();

            employeeRepository.save(employee);

            // create a jwt using the employee and send it with authentication response
            String jwt = jwtGenerationService.generateJwt(username, employee.getRoles());

            jwtList.add(jwt);
        }
        return AuthenticationResponseDto.builder().tokenList(jwtList).build();
    }

    public String login(LoginRequestDto loginRequestDto) {

        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        try {
            // it is secure after using this method, throws an exception if it is incorrect
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // if the authentication successful fetch the roles
            Set<Roles> roles = employeeRepository.findByUsername(username).get().getRoles();

            // create a jwt
            String jwt = jwtGenerationService.generateJwt(username, roles);

            // send the response with a JWT created
            return jwt;

        } catch (AuthenticationException exception) {
            throw new IllegalStateException("Bad credentials");
        }
    }


    @Transactional
    public String deleteEmployeeById(Long id) {
        if(!employeeRepository.existsById(id)) throw new IllegalStateException(id + " does not exists");

        else {
            employeeRepository.setDeletedTrue(id);
            return "employee deleted";
        }
    }

    @Transactional
    public String updateEmployee(Long id, UpdateRequestDto updateRequestDto) {

        String username = updateRequestDto.getUsername();

        String password = null;
        if(updateRequestDto.getPassword() != null) {
             password = passwordEncoder.encode(updateRequestDto.getPassword());
        }

        String email = updateRequestDto.getEmail();
        Set<Roles> rolesSet = getRolesSetRoleFromDtoSet(id, updateRequestDto.getRoleSet());
        employeeRepository.updateEmployeeById(id, username, password, email, rolesSet);
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

    private Set<Roles> getRolesSetRoleFromDtoSet(Long id, Set<RegisterRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();
        Employee employee = employeeRepository.findById(id).orElseThrow();
        for (RegisterRequestDto.RoleDto roleDto : roleDtoSet) {

            Roles role = new Roles(employee, roleDto.getRoleName());
            newRolesSet.add(role);
        }
        return newRolesSet;
    }
}

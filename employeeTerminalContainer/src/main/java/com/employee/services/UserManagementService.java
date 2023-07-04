package com.employee.services;

import com.common.JwtDto;
import com.employee.dto.LoginRequestDto;
import com.employee.dto.RegisterRequestDto;
import com.employee.dto.UpdateRequestDto;
import com.employee.exceptions.NoEmployeeWithIdException;
import com.employee.exceptions.NoRolesException;
import com.employee.exceptions.TakenUserNameException;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.RolesRepository;
import com.employee.model.Employee;
import com.employee.model.Roles;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserManagementService implements com.employee.interfaces.UserManagementService {

    @Value("${url.security.login}")
    String securityLoginUrl;
    @Value("${url.security.userManagement}")
    String securityUserManagementUrl;

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final SecurityContainerService securityContainerService;

    //-----------------------------------------------------------------------------------------------

    /**
     * Registers multiple employees with the provided user details and saves them to the database.
     *
     * @param authorizationHeader    The authorization header containing the authentication token.
     * @param registerRequestDtoList The list of RegisterRequestDto objects containing the employee details for registration.
     * @return The set of registered employees.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     * @throws TakenUserNameException  if the username is already taken by an existing employee.
     * @throws NoRolesException        if an employee does not have at least one role assigned.
     */
    @Override
    public Set<Employee> registerEmployee(String authorizationHeader, List<RegisterRequestDto> registerRequestDtoList) throws JsonProcessingException {

        securityContainerService.jwtValidation(authorizationHeader, securityUserManagementUrl);

        Set<Employee> employeeSet = new HashSet<>();

        for(RegisterRequestDto registerRequestDto: registerRequestDtoList) {

            Optional<Employee> employeeControl = employeeRepository.findByUsername(registerRequestDto.getUsername());

            if(employeeControl.isPresent()) {
                throw new TakenUserNameException("Username: " + employeeControl.get().getUsername() + " is taken");
            }

            String  username = registerRequestDto.getUsername();

            if(registerRequestDto.getRoleSet().size() == 0) {
                throw new NoRolesException("An employee mush have at least one role");
            }

            Employee employee = new Employee();
            employee.setUsername(username);
            employee.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            employee.setEmail(registerRequestDto.getEmail());
            employee.setRoles(getRolesSetFromRegisterRoleDtoSet(employee, registerRequestDto.getRoleSet()));

            employeeSet.add(employeeRepository.save(employee));
        }
        return employeeSet;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Registers multiple admin employees with the provided user details and saves them to the database.
     *
     * @param registerRequestDtoList The list of RegisterRequestDto objects containing the admin employee details for registration.
     * @return The set of registered admin employees.
     * @throws TakenUserNameException if the username is already taken by an existing employee.
     * @throws NoRolesException       if an employee does not have at least one role assigned.
     */
    public Set<Employee> registerAdmin(List<RegisterRequestDto> registerRequestDtoList) {

        Set<Employee> employeeSet = new HashSet<>();

        for(RegisterRequestDto registerRequestDto: registerRequestDtoList) {

            Optional<Employee> employeeControl = employeeRepository.findByUsername(registerRequestDto.getUsername());

            // check if the user with that username exists
            if(employeeControl.isPresent()) {
                throw new TakenUserNameException("Username: " + employeeControl.get().getUsername() + " is taken");
            }
            // check if the register request contains at least one role
            if(registerRequestDto.getRoleSet().size() == 0) {
                throw new NoRolesException("An employee mush have at least one role");
            }

            // if not create an employee
            String  username = registerRequestDto.getUsername();
            Employee employee = new Employee();
            employee.setUsername(username);
            employee.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            employee.setEmail(registerRequestDto.getEmail());
            employee.setRoles(getRolesSetFromRegisterRoleDtoSet(employee, registerRequestDto.getRoleSet()));

            employeeSet.add(employeeRepository.save(employee));
        }
        return employeeSet;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     Performs a login request with the provided login credentials and retrieves a JWT token.
     @param loginRequestDto The LoginRequestDto object containing the login credentials.
     @return A JwtDto object containing the JWT token retrieved from the login response.
     */
    @Override
    public JwtDto login(LoginRequestDto loginRequestDto) {

        ResponseEntity<JwtDto> jwtResponse = securityContainerService.login(loginRequestDto, securityLoginUrl);

        JwtDto jwtDto = jwtResponse.getBody();

        return jwtDto;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Deletes an employee with the specified ID by marking it as deleted in the database.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param id                  The ID of the employee to delete.
     * @return The ID of the deleted employee.
     * @throws JsonProcessingException   if an error occurs during JSON processing.
     * @throws NoEmployeeWithIdException if no employee with the specified ID exists.
     */
    @Transactional
    @Override
    public Long deleteEmployeeById(String authorizationHeader, Long id) throws JsonProcessingException {

        securityContainerService.jwtValidation(authorizationHeader, securityUserManagementUrl);

        if(!employeeRepository.existsById(id)) throw new NoEmployeeWithIdException("Employee with id " + id + " does not exists");

        else {
            employeeRepository.setDeletedTrue(id);
            return id;
        }
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Updates the details of an employee with the specified ID.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param id                  The ID of the employee to update.
     * @param updateRequestDto    The UpdateRequestDto object containing the updated employee details.
     * @return The updated Employee object.
     * @throws JsonProcessingException    if an error occurs during JSON processing.
     * @throws NoEmployeeWithIdException      if no employee with the specified ID exists.
     */
    @Transactional
    @Override
    public Employee updateEmployee(String authorizationHeader, Long id, UpdateRequestDto updateRequestDto) throws JsonProcessingException {

        securityContainerService.jwtValidation(authorizationHeader, securityUserManagementUrl);

        if(!employeeRepository.existsById(id)) throw new NoEmployeeWithIdException("Employee with id " + id + " does not exists"); // string builder

        String username = updateRequestDto.getUsername();

        String password = null;
        if(updateRequestDto.getPassword() != null) {
             password = updateRequestDto.getPassword();
        }

        String email = updateRequestDto.getEmail();

        employeeRepository.updateEmployeeById(id, username, password, email);


        Employee employee = employeeRepository.findById(id).get();

        Set<Roles> rolesSet = getRolesSetFromUpdateRoleDtoSet(employee, updateRequestDto.getRoleSet());

        if(rolesSet.size() != 0) {
            rolesRepository.deleteRoleById(employee.getId());
            employee.updateRoles(rolesSet);
        }
        employeeRepository.save(employee);

        return employee;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     Converts a Set of RegisterRequestDto.RoleDto objects into a Set of Roles objects associated with the provided employee.
     @param employee The Employee object to associate with the roles.
     @param roleDtoSet A Set of RegisterRequestDto.RoleDto objects containing role data.
     @return A Set of Roles objects associated with the provided employee and extracted from the roleDtoSet.
     */
    public Set<Roles> getRolesSetFromRegisterRoleDtoSet(Employee employee, Set<RegisterRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();

        for (RegisterRequestDto.RoleDto roleDto : roleDtoSet) {

            Roles role = new Roles(employee, roleDto.getRoleName());
            //Roles role = Roles.builder().employee(employee).roleName(roleDto.getRoleName()).build();
            newRolesSet.add(role);
        }
        return newRolesSet;
    }

    private Set<Roles> getRolesSetFromUpdateRoleDtoSet(Employee employee, Set<UpdateRequestDto.RoleDto> roleDtoSet) {
        Set<Roles> newRolesSet = new HashSet<>();

        for (UpdateRequestDto.RoleDto roleDto : roleDtoSet) {
            Roles role = new Roles(employee, roleDto.getRoleName());
            newRolesSet.add(role);
        }
        return newRolesSet;
    }
}

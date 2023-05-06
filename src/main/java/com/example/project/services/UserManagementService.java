package com.example.project.services;

import com.example.project.dto.AuthenticationResponseDto;
import com.example.project.dto.RegisterRequestDto;
import com.example.project.dto.UpdateRequestDto;
import com.example.project.model.Employee;
import com.example.project.model.Roles;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    PasswordEncoder passwordEncoder;

    //--------------------------------------------------

    public AuthenticationResponseDto registerNewEmployee(RegisterRequestDto registration) {

        Optional<Employee> employeeControl = employeeRepository.findByUsername(registration.getUsername());

        // check if the user with that username exists
        if(employeeControl.isPresent()) {
            throw new IllegalStateException("Username taken");
        }

        // if not create an employee
        String  username = registration.getUsername();

        Employee employee = Employee.builder()
                .username(username)
                .password(passwordEncoder.encode(registration.getPassword()))
                .email(registration.getEmail())
                .roles(getRolesSet(registration.getRoles()))
                .build();

        employeeRepository.save(employee);

        Set<Roles> employeeRolesSet = getRolesSet(registration.getRoles());


        // create a jwt using the employee and send it with authentication response
        String jwt = jwtGenerationService.generateJwt(username, employeeRolesSet);

        return AuthenticationResponseDto.builder().token(jwt).build();
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
    public String updateEmployee(UpdateRequestDto updateRequestDto) {

        Long id = updateRequestDto.getId();
        String username = updateRequestDto.getUsername();
        String password = passwordEncoder.encode(updateRequestDto.getPassword());
        String email = updateRequestDto.getEmail();
        Set<Roles> rolesSet = getRolesSet(updateRequestDto.getRoles());
        //employeeRepository.updateEmployeeById(username, password, email, rolesSet, id);
        //employeeRepository.updateRolesById(rolesSet, id);
        employeeRepository.updateWithoutRoles(username, password, email, id);
        return "employee updated";
    }

    //-----------------------------------------------------

    // create a Set<roles> from the array of String roles received from RegisterRequestDto
    private Set<Roles> getRolesSet(String[] roles) {
        Set<Roles> rolesSet = new HashSet<>();
        for(String roleStr: roles) {
            // rolesSet.add(rolesRepository.findByRoleName(roleStr));
            rolesSet.add(new Roles(roleStr));
        }
        return rolesSet;
    }
}

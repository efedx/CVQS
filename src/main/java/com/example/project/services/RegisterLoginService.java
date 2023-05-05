package com.example.project.services;

import com.example.project.model.Employee;
import com.example.project.model.Roles;
import com.example.project.repository.EmployeeRepository;
import com.example.project.dto.LoginRequestDto;
import com.example.project.repository.RolesRepository;
import com.example.project.dto.AuthenticationResponseDto;
import com.example.project.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterLoginService {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final RolesRepository rolesRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtGenerationService jwtGenerationService;

    public AuthenticationResponseDto register(RegisterRequestDto registerRequestDto) {

        // create an employee and save it to the database
        Employee employee = Employee.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .roles(getRoles(registerRequestDto.getRoles()))
                .build();

        employeeRepository.save(employee);

        String username = employee.getUsername();
        Set<Roles> roles = employee.getRoles();

        // create a jwt using the employee and send it with authentication response
        String jwt = jwtGenerationService.generateJwt(username, roles);
        return AuthenticationResponseDto.builder().token(jwt).build();
        }


    public AuthenticationResponseDto login(LoginRequestDto loginRequestDto) {

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
            return AuthenticationResponseDto.builder().token(jwt).build();

        }

        catch (AuthenticationException exception) {
            throw new IllegalStateException("Bad credentials");
        }
    }

    // to create a roles set from string
    private Set<Roles> getRoles(String[] rolesStr) {
        Set<Roles> rolesSet = new HashSet<>();
        for(String role: rolesStr) {
            rolesSet.add(rolesRepository.findByRoleName(role));
        }
        return rolesSet;
    }
}

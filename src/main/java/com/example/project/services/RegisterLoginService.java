package com.example.project.services;

import com.example.project.model.Employee;
import com.example.project.model.Roles;
import com.example.project.repository.EmployeeRepository;
import com.example.project.security.AuthenticationRequest;
import com.example.project.security.AuthenticationResponse;
import com.example.project.security.RegisterRequest;
import com.example.project.security.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RegisterLoginService {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtGenerationService jwtGenerationService;

    public AuthenticationResponse register(RegisterRequest request) {

        // create an employee and save it to the database
        Employee employee = Employee.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(commaSeperatedStringtoSet(request.getRoles())).build();
        employeeRepository.save(employee);

        // create a jwt using the employee and send it with authentication response
        String jwt = jwtGenerationService.generateJwt(employee);
        return AuthenticationResponse.builder().token(jwt).build();
        }

    private Set<Roles> commaSeperatedStringtoSet(String roles) {
        return Stream.of(roles.trim().split("[, ]")).map(s -> RoleEnum.valueOf(s)).collect(Collectors.toSet());
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        // it is secure after using this method, throws an exception if it is incorrect
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        // fetch the employee from the database based on the username on the request
        Employee employee = employeeRepository.findByUsername(request.getUsername()) // todo username-email
                .orElseThrow();
        // create a jwt using the employee from the database
        String jwt = jwtGenerationService.generateJwt(employee);

        return AuthenticationResponse.builder().token(jwt).build();
    };
}

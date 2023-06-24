package com.example.services;

import com.example.dto.LoginRequestDto;
import com.example.exceptions.CustomBadCredentialsException;
import com.example.model.Employee;
import com.example.model.Roles;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementService {

    @Autowired
    private JwtGenerationService jwtGenerationService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeRepository employeeRepository;

    public String login(LoginRequestDto loginRequestDto) {

        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        try {
            // it is secure after using this method, throws an exception if it is incorrect
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            Employee employee = employeeRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
            // if the authentication successful fetch the roles
            Set<Roles> rolesSet = employee.getRoles();
            String name = employee.getUsername();
            String jwt = jwtGenerationService.generateJwt(name, rolesSet);
            return jwt;

            // send the response with a JWT created

        } catch (AuthenticationException exception) {
            throw new CustomBadCredentialsException("Bad credentials");
        }
    }
}

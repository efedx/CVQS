package com.example.project.services;

import com.example.project.model.Employee;
import com.example.project.repository.EmployeeRepository;
import com.example.project.security.AuthenticationRequest;
import com.example.project.security.AuthenticationResponse;
import com.example.project.security.RegisterRequest;
import com.example.project.security.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.example.project.security.SecurityConstans.JWT_HEADER;
import static com.example.project.security.SecurityConstans.JWT_KEY;

@Service
@RequiredArgsConstructor
public class RegisterLoginService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerationService jwtGenerationService;
    public AuthenticationResponse register(RegisterRequest request) {

        // create an employee and save it to the database
        Employee employee = Employee.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        employeeRepository.save(employee);

        // create a jwt using the employee and send it with authentication response
        String jwt = jwtGenerationService.generateJwt(employee);
        return AuthenticationResponse.builder().token(jwt).build();
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

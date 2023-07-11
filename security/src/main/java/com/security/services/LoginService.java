package com.security.services;

import com.security.dtos.LoginRequestDto;
import com.security.exceptions.CustomBadCredentialsException;
import com.security.entities.Employee;
import com.security.entities.Roles;
import com.security.exceptions.CustomUsernameNotFoundException;
import com.security.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService implements com.security.interfaces.LoginService {

    private final JwtGenerationService jwtGenerationService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;

    //-----------------------------------------------------------------------------------------------

    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     *
     * @param loginRequestDto The DTO object containing the username and password for login.
     * @return The generated JWT token as a string.
     * @throws CustomBadCredentialsException If the provided credentials are invalid.
     * @throws CustomUsernameNotFoundException    If the user is not found with the given username.
     */
    @Override
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
                    .orElseThrow(() -> new CustomUsernameNotFoundException("User not found with username : " + username));
            // if the authentication successful fetch the roles
            Set<Roles> rolesSet = employee.getRoles();
            String name = employee.getUsername();
            String jwt = jwtGenerationService.generateJwt(name, rolesSet);
            return jwt;

        } catch (AuthenticationException exception) {
            throw new CustomBadCredentialsException("Bad credentials");
        }
    }
}

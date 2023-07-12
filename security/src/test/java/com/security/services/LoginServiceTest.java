package com.security.services;

import com.security.dtos.LoginRequestDto;
import com.security.entities.Employee;
import com.security.entities.Roles;
import com.security.exceptions.CustomBadCredentialsException;
import com.security.exceptions.CustomUsernameNotFoundException;
import com.security.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    JwtGenerationService jwtGenerationService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    LoginService underTestLoginService;

    @Test
    void login() {

        // given
        String username = "username";
        String password = "password";
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username(username).password(password).build();

        Employee employee = new Employee();
        employee.setUsername(username);
        Roles role = Roles.builder()
                .employee(employee)
                .roleName("ROLE_ADMIN")
                .build();
        Set<Roles> roleSet = Set.of(role);
        employee.setRoles(roleSet);

        // when
        when(employeeRepository.findByUsername(username)).thenReturn(Optional.of(employee));
        when(jwtGenerationService.generateJwt(username, roleSet)).thenReturn("token");

        ArgumentCaptor<String> usernameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Set> roleSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);

        underTestLoginService.login(loginRequestDto);

        // then
        verify(jwtGenerationService).generateJwt(usernameArgumentCaptor.capture(), roleSetArgumentCaptor.capture());

        String capturedUserName= usernameArgumentCaptor.getValue();
        Set<Roles> capturedRoleSet = roleSetArgumentCaptor.getValue();

        assertThat(capturedUserName).isEqualTo(employee.getUsername());
        assertThat(capturedRoleSet.size()).isEqualTo(employee.getRoles().size());

    }

    @Test
    void shouldThrowCustomUsernameNotFoundException() {

        // given
        String username = "username";
        String password = "password";
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username(username).password(password).build();

        // when
        when(employeeRepository.findByUsername(username)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTestLoginService.login(loginRequestDto))
                .isInstanceOf(CustomUsernameNotFoundException.class)
                .hasMessage("User not found with username: " + username);
    }

    @Test
    void shouldThrowCustomBadCredentialsException() {
        // given
        String username = "username";
        String password = "password";
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username(username).password(password).build();

        // when
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(AuthenticationException.class);
        // then
//        assertThatThrownBy(() -> underTestLoginService.login(loginRequestDto))
//                .isInstanceOf(CustomBadCredentialsException.class)
//                .hasMessage("Bad credentials");

        assertThrows(CustomBadCredentialsException.class, () -> {
            underTestLoginService.login(loginRequestDto);
        });
    }
}
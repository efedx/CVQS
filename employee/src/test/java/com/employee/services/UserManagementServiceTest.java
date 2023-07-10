package com.employee.services;

import com.employee.entities.Employee;
import com.employee.dto.LoginRequestDto;
import com.employee.dto.RegisterRequestDto;
import com.employee.dto.UpdateRequestDto;
import com.employee.exceptions.TakenUserNameException;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.RolesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@Import(ConfigurationForTests.class)
//@ContextConfiguration(classes = ConfigurationForTests.class)
class UserManagementServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RolesRepository rolesRepository;
    @InjectMocks
    UserManagementService underTestUserManagementService;

    @BeforeEach
    void setUp() {
//        underTestUserManagementService = new UserManagementService(employeeRepository, restTemplate, passwordEncoder);
    }

    @Test
    void registerEmployee() throws JsonProcessingException {

        RegisterRequestDto.RoleDto role1 = new RegisterRequestDto.RoleDto();
        role1.setRoleName("OPERATOR");
        RegisterRequestDto.RoleDto role2 = new RegisterRequestDto.RoleDto();
        role2.setRoleName("ADMIN");

        Set<RegisterRequestDto.RoleDto> roleDtoSet = new HashSet<>();
        roleDtoSet.add(role1);
        roleDtoSet.add(role2);

        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUsername("username");
        registerRequestDto.setPassword("password");
        registerRequestDto.setEmail("user@email.com");
        registerRequestDto.setRoleSet(roleDtoSet);
        registerRequestDto.setDepartment("department");
        registerRequestDto.setTerminal("terminal");

        List<RegisterRequestDto> registerRequestDtoList = new ArrayList<>();
        registerRequestDtoList.add(registerRequestDto);

        Employee employee = new Employee();
        employee.setUsername(registerRequestDto.getUsername());
        employee.setPassword(registerRequestDto.getPassword());
        employee.setEmail(registerRequestDto.getEmail());
        employee.setRoles(underTestUserManagementService.getRolesSetFromRegisterRoleDtoSet(employee, registerRequestDto.getRoleSet()));
        employee.setDepartment(registerRequestDto.getDepartment());
        employee.setTerminal(registerRequestDto.getTerminal());

        // when
        when(employeeRepository.findByUsername(registerRequestDto.getUsername())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(passwordEncoder.encode(any(String.class))).thenReturn(registerRequestDto.getPassword());


        Set<Employee> employeeSet = underTestUserManagementService.registerEmployee(registerRequestDtoList);

        // then
        assertThat(employeeSet).contains(employee);
        Employee employeeItereator = employeeSet.iterator().next();
        assertThat(registerRequestDto.getUsername()).isEqualTo(employee.getUsername());
        assertThat(registerRequestDto.getEmail()).isEqualTo(employee.getEmail());
        assertThat(registerRequestDto.getPassword()).isEqualTo(employee.getPassword());
        assertThat(registerRequestDto.getRoleSet().size()).isEqualTo(employee.getRoles().size());
        assertThat(registerRequestDto.getDepartment()).isEqualTo(employee.getDepartment());
        assertThat(registerRequestDto.getTerminal()).isEqualTo(employee.getTerminal());
    }

    @Test
    void shouldThrowTakenUsernameExceptionAtRegisterEmployee() {
        // given
        String username = "username";

        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder().username(username).build();
        List<RegisterRequestDto> registerRequestDtoList = List.of(registerRequestDto);

        Employee employeeControl = Employee.builder().username(username).build();
        given(employeeRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(employeeControl));

        // when

        // assert
        assertThatThrownBy(() -> underTestUserManagementService.registerEmployee(registerRequestDtoList))
                .isInstanceOf(TakenUserNameException.class)
                .hasMessage("Username: " + username + " is taken");
    }

    @Test
    void shouldThrowNoExceptionAtRegisterEmployee() {
        // given
        String username = "username";

        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder().username(username).build();
        List<RegisterRequestDto> registerRequestDtoList = List.of(registerRequestDto);

        Employee employeeControl = Employee.builder().username(username).build();
        given(employeeRepository.findByUsername(anyString())).willReturn(Optional.of(employeeControl));

        // when

        // assert
        assertThatThrownBy(() -> underTestUserManagementService.registerEmployee(registerRequestDtoList))
                .isInstanceOf(TakenUserNameException.class)
                .hasMessage("Username: " + username + " is taken");
    }

    @Test
    void login() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("username").password("password").build();

        ArgumentCaptor<HttpEntity<LoginRequestDto>> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto);

        // when

        // then

        HttpEntity<LoginRequestDto> capturedRequestEntity = requestEntityCaptor.getValue();
        LoginRequestDto capturedLoginRequestDto = capturedRequestEntity.getBody();

        //assert capturedLoginRequestDto != null;
        assertThat("username").isEqualTo(capturedLoginRequestDto.getUsername());
        assertThat("password").isEqualTo(capturedLoginRequestDto.getPassword());
    }

    @Test
    void deleteEmployeeById() throws JsonProcessingException {
        // given
        Long id = anyLong();
        given(employeeRepository.existsById(id)).willReturn(true);

        // when
        underTestUserManagementService.deleteEmployeeById(id);

        // then
        verify(employeeRepository).setDeletedTrue(id);

    }

    @Test
    void shouldThrowErrorDeleteStudent() {
        // given
        Long id = 1L;
        given(employeeRepository.existsById(anyLong())).willReturn(false);
        // when

        // then
        assertThatThrownBy(() -> underTestUserManagementService.deleteEmployeeById(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Employee with id " + id + " does not exists");

        verify(employeeRepository, never()).setDeletedTrue(anyLong());
    }

    @Test
    void updateEmployee() throws JsonProcessingException {
        // given
        Long id = 1L;
        String username = "username";
        String email = "email@gmail.com";
        String password = "password";
        Employee employee = Employee.builder().build();

        UpdateRequestDto updateRequestDto = UpdateRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        given(employeeRepository.existsById(id)).willReturn(true);
        given(employeeRepository.findById(anyLong())).willReturn(Optional.ofNullable(employee));
        // when
        Employee employeeTest = underTestUserManagementService.updateEmployee(id, updateRequestDto);
        when(passwordEncoder.encode(any())).thenReturn(updateRequestDto.getPassword());
        //then
        verify(employeeRepository).updateEmployeeById(id, username, passwordEncoder.encode(password), email);
    }

    @Test
    void shouldThrowErrorUpdateStudent() {
        // given
        Long id = 1L;
        String username = "username";
        String email = "email@gmail.com";
        String password = "password";
        Employee employee = Employee.builder().build();

        UpdateRequestDto updateRequestDto = UpdateRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        given(employeeRepository.existsById(id)).willReturn(false);
        // when

        // then
        assertThatThrownBy(() -> underTestUserManagementService.updateEmployee(id, updateRequestDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Employee with id " + id + " does not exists");

        verify(employeeRepository, never()).updateEmployeeById(anyLong(), anyString(), anyString(), anyString());
    }
}
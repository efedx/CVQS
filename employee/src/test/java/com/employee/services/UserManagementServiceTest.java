package com.employee.services;

import com.employee.dto.JwtDto;
import com.employee.entities.Employee;
import com.employee.dto.LoginRequestDto;
import com.employee.dto.RegisterRequestDto;
import com.employee.dto.UpdateRequestDto;
import com.employee.exceptions.NoEmployeeWithIdException;
import com.employee.exceptions.NoRolesException;
import com.employee.exceptions.TakenUserNameException;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.RolesRepository;
import com.employee.securityClient.SecurityClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RolesRepository rolesRepository;
    @Mock
    SecurityClient securityClient;
    @InjectMocks
    UserManagementService underTestUserManagementService;

    @BeforeEach
    void setUp() {
        // Instantiating password encoder before every test, now I get the output
        this.passwordEncoder = new BCryptPasswordEncoder();
        underTestUserManagementService = new UserManagementService(employeeRepository, passwordEncoder, rolesRepository, securityClient);
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

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        // when
        when(employeeRepository.findByUsername(registerRequestDto.getUsername())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        //when(passwordEncoder.encode(any(String.class))).thenReturn(registerRequestDto.getPassword());

        underTestUserManagementService.registerEmployee(registerRequestDtoList); //Set<Employee> employeeSet =

        // then

        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();

        Boolean passwordsMatch = passwordEncoder.matches("password", capturedEmployee.getPassword());
        assertThat(passwordsMatch).isTrue();

        assertThat(capturedEmployee.getUsername()).isEqualTo(registerRequestDto.getUsername());
        assertThat(capturedEmployee.getEmail()).isEqualTo(registerRequestDto.getEmail());
        assertThat(capturedEmployee.getRoles().size()).isEqualTo(registerRequestDto.getRoleSet().size());
        assertThat(capturedEmployee.getDepartment()).isEqualTo(registerRequestDto.getDepartment());
        assertThat(capturedEmployee.getTerminal()).isEqualTo(registerRequestDto.getTerminal());
    }

    @Test
    void shouldThrowTakenUsernameExceptionAtRegisterEmployee() {
        // given
        String username = "username";

        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder().username(username).build();
        List<RegisterRequestDto> registerRequestDtoList = List.of(registerRequestDto);

        Employee employeeControl = Employee.builder().username(username).build();


        // when
        when(employeeRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(employeeControl));
        // assert
        assertThatThrownBy(() -> underTestUserManagementService.registerEmployee(registerRequestDtoList))
                .isInstanceOf(TakenUserNameException.class)
                .hasMessage("Username: " + username + " is taken");
    }

    @Test
    void shouldThrowNoRolesAtRegisterEmployee() throws JsonProcessingException {
        // given
        Set<RegisterRequestDto.RoleDto> roleDtoSet = new HashSet<>();
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder().roleSet(roleDtoSet).build();
        List<RegisterRequestDto> registerRequestDtoList = List.of(registerRequestDto);
        // when
        // assert
        assertThatThrownBy(() -> underTestUserManagementService.registerEmployee(registerRequestDtoList))
                .isInstanceOf(NoRolesException.class)
                .hasMessage("An employee mush have at least one role");
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

        ArgumentCaptor<LoginRequestDto> loginRequestDtoArgumentCaptor = ArgumentCaptor.forClass(LoginRequestDto.class);

        // when
        ResponseEntity<JwtDto> jwtDtoResponseEntity = new ResponseEntity<>(HttpStatusCode.valueOf(200));
        when(securityClient.login(any(LoginRequestDto.class))).thenReturn(jwtDtoResponseEntity);
        underTestUserManagementService.login(loginRequestDto);

        // then
        verify(securityClient).login(loginRequestDtoArgumentCaptor.capture());
        LoginRequestDto capturedLoginRequestDto = loginRequestDtoArgumentCaptor.getValue();

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
                .isInstanceOf(NoEmployeeWithIdException.class)
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
        String department = "department";
        String terminal = "terminal";

        UpdateRequestDto.RoleDto roleDto = new UpdateRequestDto.RoleDto();
        roleDto.setRoleName("role");
        Set<UpdateRequestDto.RoleDto> roleDtoSet = Set.of(roleDto);

        Employee employee = new Employee();
        employee.setRoles(underTestUserManagementService.getRolesSetFromUpdateRoleDtoSet(employee, roleDtoSet));

        UpdateRequestDto updateRequestDto = UpdateRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .roleSet(roleDtoSet)
                .department(department)
                .terminal(terminal)
                .build();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        when(employeeRepository.existsById(id)).thenReturn(true);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(employee));
        //when(rolesRepository.deleteRoleById(id)).thenReturn()
        underTestUserManagementService.updateEmployee(id, updateRequestDto);


        //then
//        boolean passwordsMatches = passwordEncoder.matches()

        verify(employeeRepository).updateEmployeeById(eq(id), eq(username), stringArgumentCaptor.capture(), eq(email), eq(department), eq(terminal));
        String encodedPassword = stringArgumentCaptor.getValue();

        Boolean passwordsMatch = passwordEncoder.matches(password, encodedPassword);
        assertThat(passwordsMatch).isTrue();
    }

    @Test
    void shouldThrowErrorUpdateStudent() {
        // given
        Long id = 1L;

        UpdateRequestDto updateRequestDto = UpdateRequestDto.builder().build();
        given(employeeRepository.existsById(id)).willReturn(false);
        // when

        // then
        assertThatThrownBy(() -> underTestUserManagementService.updateEmployee(id, updateRequestDto))
                .isInstanceOf(NoEmployeeWithIdException.class)
                .hasMessage("Employee with id " + id + " does not exists");

        verify(employeeRepository, never()).updateEmployeeById(anyLong(), anyString(), anyString(), anyString(), anyString(), anyString());
    }
}
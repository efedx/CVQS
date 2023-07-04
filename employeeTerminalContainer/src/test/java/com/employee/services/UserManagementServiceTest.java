package com.employee.services;

import com.common.Employee;
import com.common.LoginRequestDto;
import com.common.RegisterRequestDto;
import com.employee.dto.UpdateRequestDto;
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
    @Mock
    SecurityContainerService securityContainerService;
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

        List<RegisterRequestDto> registerRequestDtoList = new ArrayList<>();
        registerRequestDtoList.add(registerRequestDto);

        Employee employee = new Employee();
        employee.setUsername(registerRequestDto.getUsername());
        employee.setPassword(registerRequestDto.getPassword());
        employee.setEmail(registerRequestDto.getEmail());
        employee.setRoles(underTestUserManagementService.getRolesSetFromRegisterRoleDtoSet(employee, registerRequestDto.getRoleSet()));

        // when
        when(employeeRepository.findByUsername(registerRequestDto.getUsername())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(passwordEncoder.encode(any(String.class))).thenReturn(registerRequestDto.getPassword());
//        doNothing().when(securityContainerService).jwtValidation(anyString(), anyString());


        Set<Employee> employeeSet = underTestUserManagementService.registerEmployee("token", registerRequestDtoList);

        // then
        assertThat(employeeSet).contains(employee);
        Employee employeeItereator = employeeSet.iterator().next();
        assertThat(registerRequestDto.getUsername()).isEqualTo(employee.getUsername());
        assertThat(registerRequestDto.getEmail()).isEqualTo(employee.getEmail());
        assertThat(registerRequestDto.getPassword()).isEqualTo(employee.getPassword());
        assertThat(registerRequestDto.getRoleSet().size()).isEqualTo(employee.getRoles().size());
    }

    @Test
    void login() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("username").password("password").build();

        ArgumentCaptor<HttpEntity<LoginRequestDto>> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto);

        // when
        //ResponseEntity<JwtDto> jwtResponse = restTemplate.exchange("securityLoginUrl", HttpMethod.POST, requestEntity, JwtDto.class);

        // then
        //Mockito.verify(restTemplate).exchange(eq("securityLoginUrl"), eq(HttpMethod.POST), requestEntityCaptor.capture(), eq(JwtDto.class));

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
        underTestUserManagementService.deleteEmployeeById("token", id);

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
        assertThatThrownBy(() -> underTestUserManagementService.deleteEmployeeById("token", id))
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
        Employee employeeTest = underTestUserManagementService.updateEmployee("token", id, updateRequestDto);
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
        assertThatThrownBy(() -> underTestUserManagementService.updateEmployee("token", id, updateRequestDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Employee with id " + id + " does not exists");

        verify(employeeRepository, never()).updateEmployeeById(anyLong(), anyString(), anyString(), anyString());
    }
}
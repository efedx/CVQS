package com.security.services;

import com.security.dto.*;
import com.security.model.Employee;
import com.security.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

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
    RestTemplate restTemplate;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserManagementService underTestUserManagementService;

//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @BeforeEach
    void setUp() {
//        underTestUserManagementService = new UserManagementService(employeeRepository, restTemplate, passwordEncoder);
    }

    @Test
    void registerEmployee() {

        RegisterRequestDto.RoleDto role1 = new RegisterRequestDto.RoleDto();
        role1.setRoleName("OPERATOR");
        RegisterRequestDto.RoleDto role2 = new RegisterRequestDto.RoleDto();
        role2.setRoleName("ADMIN");

        // Create a Set of RoleDto objects
        Set<RegisterRequestDto.RoleDto> roleDtoSet1 = new HashSet<>();
        roleDtoSet1.add(role1);
        roleDtoSet1.add(role2);

//        Set<RegisterRequestDto.RoleDto> roleDtoSet2 = new HashSet<>();
//        roleDtoSet2.add(role1);

        // Create RegisterRequestDto object and set values
        RegisterRequestDto registerRequestDto1 = new RegisterRequestDto();
        registerRequestDto1.setUsername("username1");
        registerRequestDto1.setPassword("password1");
        registerRequestDto1.setEmail("user1@email.com");
        registerRequestDto1.setRoleSet(roleDtoSet1);

//        RegisterRequestDto registerRequestDto2 = new RegisterRequestDto();
//        registerRequestDto2.setUsername("username2");
//        registerRequestDto2.setPassword("password2");
//        registerRequestDto2.setEmail("user2@email.com");
//        registerRequestDto2.setRoleSet(roleDtoSet2);

        given(passwordEncoder.encode(any())).willReturn(registerRequestDto1.getPassword());

        List<RegisterRequestDto> registerRequestDtoList = new ArrayList<>();
        registerRequestDtoList.add(registerRequestDto1);

        Employee employee = new Employee();
        employee.setUsername(registerRequestDto1.getUsername());
        employee.setPassword(passwordEncoder.encode(registerRequestDto1.getPassword()));
        employee.setEmail(registerRequestDto1.getEmail());
        employee.setRoles(underTestUserManagementService.getRolesSetFromRegisterRoleDtoSet(employee, registerRequestDto1.getRoleSet()));

//        Set<RegisterRequestDto.RoleDto> roleDtoSet = Set.of(new RegisterRequestDto(username, password, email,
//                Set.of(new RegisterRequestDto.RoleDto("ADMIN"))));

         //given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class) , eq(Object.class))).willReturn(ResponseEntity.ok("true"));

        // when
        Mockito.when(employeeRepository.findByUsername(registerRequestDto1.getUsername())).thenReturn(Optional.empty());
        Mockito.when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Set<Employee> employeeSet = underTestUserManagementService.registerEmployee("token", registerRequestDtoList);

        Set<Employee> result = underTestUserManagementService.registerAdmin(registerRequestDtoList);

        // then
        assertThat(result).contains(employee);
        Employee employeeItereator = result.iterator().next();
        assertThat(registerRequestDto1.getUsername()).isEqualTo(employee.getUsername());
        assertThat(registerRequestDto1.getEmail()).isEqualTo(employee.getEmail());
        assertThat(registerRequestDto1.getPassword()).isEqualTo(employee.getPassword());
        assertThat(registerRequestDto1.getRoleSet().size()).isEqualTo(employee.getRoles().size());
    }

    @Test
    void login() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("username").password("password").build();

        ArgumentCaptor<HttpEntity<LoginRequestDto>> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto);

        // when
        ResponseEntity<JwtDto> jwtResponse = restTemplate.exchange("securityLoginUrl", HttpMethod.POST, requestEntity, JwtDto.class);

        // then
        Mockito.verify(restTemplate).exchange(eq("securityLoginUrl"), eq(HttpMethod.POST), requestEntityCaptor.capture(), eq(JwtDto.class));

        HttpEntity<LoginRequestDto> capturedRequestEntity = requestEntityCaptor.getValue();
        LoginRequestDto capturedLoginRequestDto = capturedRequestEntity.getBody();

        //assert capturedLoginRequestDto != null;
        assertThat("username").isEqualTo(capturedLoginRequestDto.getUsername());
        assertThat("password").isEqualTo(capturedLoginRequestDto.getPassword());
    }

    @Test
    void deleteEmployeeById() {
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
    void updateEmployee() {
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
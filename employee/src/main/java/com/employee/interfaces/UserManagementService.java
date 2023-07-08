package com.employee.interfaces;

import com.common.Employee;
import com.common.JwtDto;
import com.common.LoginRequestDto;
import com.common.RegisterRequestDto;
import com.employee.dto.UpdateRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface UserManagementService {
    Set<Employee> registerEmployee(String authorizationHeader, List<RegisterRequestDto> registerRequestDtoList) throws JsonProcessingException;

    JwtDto login(LoginRequestDto loginRequestDto);

    @Transactional
    Long deleteEmployeeById(String authorizationHeader, Long id) throws JsonProcessingException;

    @Transactional
    Employee updateEmployee(String authorizationHeader, Long id, UpdateRequestDto updateRequestDto) throws JsonProcessingException;
}

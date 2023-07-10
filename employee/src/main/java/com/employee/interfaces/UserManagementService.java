package com.employee.interfaces;

import com.employee.entities.Employee;
import com.employee.dto.JwtDto;
import com.employee.dto.LoginRequestDto;
import com.employee.dto.RegisterRequestDto;
import com.employee.dto.UpdateRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface UserManagementService {
    Set<Employee> registerEmployee(List<RegisterRequestDto> registerRequestDtoList) throws JsonProcessingException;

    JwtDto login(LoginRequestDto loginRequestDto);

    @Transactional
    Long deleteEmployeeById(Long id) throws JsonProcessingException;

    @Transactional
    Employee updateEmployee(Long id, UpdateRequestDto updateRequestDto) throws JsonProcessingException;
}
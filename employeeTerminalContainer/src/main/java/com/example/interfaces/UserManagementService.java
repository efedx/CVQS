package com.example.interfaces;

import com.example.dto.JwtDto;
import com.example.dto.LoginRequestDto;
import com.example.dto.RegisterRequestDto;
import com.example.dto.UpdateRequestDto;
import com.example.model.Employee;
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

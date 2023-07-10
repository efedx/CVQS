package com.terminal.interfaces;

import com.terminal.dtos.RegisterTerminalDto;
import com.terminal.entities.Department;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface RegisterTerminalsService {
    Set<Department> registerTerminals(String authorizationHeader, List<RegisterTerminalDto> registerTerminalDtoList) throws JsonProcessingException;
}

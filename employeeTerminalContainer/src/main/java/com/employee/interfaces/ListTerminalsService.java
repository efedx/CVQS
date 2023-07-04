package com.employee.interfaces;

import com.employee.dto.TerminalResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

public interface ListTerminalsService {
    Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection, String terminalName) throws JsonProcessingException;

    Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection) throws JsonProcessingException;
}

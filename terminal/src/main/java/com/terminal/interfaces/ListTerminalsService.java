package com.terminal.interfaces;

import com.terminal.dtos.TerminalResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

public interface ListTerminalsService {
    Page<TerminalResponseDto> getActiveTerminalsPage(int pageNumber, String sortDirection, String terminalName) throws JsonProcessingException;

    Page<TerminalResponseDto> getActiveTerminalsPage(int pageNumber, String sortDirection) throws JsonProcessingException;
}

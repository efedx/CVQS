package com.example.interfaces;

import com.example.dto.TerminalResponseDto;
import org.springframework.data.domain.Page;

public interface ListTerminalsService {
    Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection, String terminalName);

    Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection);
}

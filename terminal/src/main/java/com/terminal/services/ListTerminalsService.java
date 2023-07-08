package com.terminal.services;

import com.terminal.dtos.TerminalResponseDto;
import com.terminal.models.Terminal;
import com.terminal.repositories.TerminalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTerminalsService implements com.terminal.interfaces.ListTerminalsService {

    private final TerminalRepository terminalRepository;
    private final RestTemplate restTemplate;

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a paginated list of active terminals with the specified terminal name.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param pageNumber         The page number to retrieve.
     * @param sortDirection      The sort direction for the results ("asc" for ascending, "desc" for descending).
     * @param terminalName       The terminal name to filter the results (can be null or empty for no filtering).
     * @return A Page object containing the list of TerminalResponseDto objects and pagination information.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    @Override
    public Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection, String terminalName) throws JsonProcessingException {

        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by("id").ascending()
                : Sort.by("id").descending());

        List<Terminal> activeTerminalsList = terminalRepository.findByActiveTerminalsAndTerminalNameList(terminalName);
        List<TerminalResponseDto> terminalResponseDtoList = new ArrayList<>();

        int totalRecords = activeTerminalsList.size();

        for(Terminal terminal: activeTerminalsList) {

            TerminalResponseDto terminalResponseDto = TerminalResponseDto.builder()
                    .id(terminal.getId())
                    .departmentName(terminal.getDepartmentName())
                    .terminalName(terminal.getTerminalName())
                    .build();
            terminalResponseDtoList.add(terminalResponseDto);
        }

        Page<TerminalResponseDto> terminalResponsePageList = new PageImpl<>(terminalResponseDtoList, pageable, totalRecords);

        return terminalResponsePageList;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a paginated list of active terminals without filtering by terminal name.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param pageNumber         The page number to retrieve.
     * @param sortDirection      The sort direction for the results ("asc" for ascending, "desc" for descending).
     * @return A Page object containing the list of TerminalResponseDto objects and pagination information.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    @Override
    public Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection) throws JsonProcessingException {

        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by("id").ascending()
                : Sort.by("id").descending());

        List<Terminal> activeTerminalsList = terminalRepository.findAllByActiveTerminalsList(pageable);
        List<TerminalResponseDto> terminalResponseDtoList = new ArrayList<>();

        int totalRecords = activeTerminalsList.size();

        for(Terminal terminal: activeTerminalsList) {

            TerminalResponseDto terminalResponseDto = TerminalResponseDto.builder()
                    .id(terminal.getId())
                    .departmentName(terminal.getDepartmentName())
                    .terminalName(terminal.getTerminalName())
                    .build();
            terminalResponseDtoList.add(terminalResponseDto);
        }

        Page<TerminalResponseDto> terminalResponsePageList = new PageImpl<>(terminalResponseDtoList, pageable, totalRecords);

        return terminalResponsePageList;
    }
}

package com.example.services;

import com.example.dto.TerminalResponseDto;
import com.example.model.Department;
import com.example.model.Terminal;
import com.example.repository.DepartmentRepository;
import com.example.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTerminalsService {

    @Value("${url.security.terminals}")
    String securityTerminalsUrl;

    @Autowired
    TerminalRepository terminalRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RestTemplate restTemplate;

    public Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection, String terminalName) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityTerminalsUrl, HttpMethod.POST, requestEntity, Object.class);

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

    public Page<TerminalResponseDto> getActiveTerminalsPage(String authorizationHeader, int pageNumber, String sortDirection) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityTerminalsUrl, HttpMethod.POST, requestEntity, Object.class);

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

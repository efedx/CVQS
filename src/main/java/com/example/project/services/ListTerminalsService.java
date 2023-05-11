package com.example.project.services;

import com.example.project.dto.TerminalResponseDto;
import com.example.project.model.Department;
import com.example.project.model.Terminal;
import com.example.project.repository.DepartmentRepository;
import com.example.project.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTerminalsService {

    @Autowired
    TerminalRepository terminalRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    public Page<TerminalResponseDto> getActiveTerminalsPage(int pageNumber, String sortDirection, String terminalName) {

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

        Page<TerminalResponseDto> terminalResponsePage = new PageImpl<>(terminalResponseDtoList, pageable, totalRecords);

        return terminalResponsePage;

    }

    public Page<TerminalResponseDto> getActiveTerminalsPage(int pageNumber, String sortDirection) {

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

        Page<TerminalResponseDto> terminalResponsePage = new PageImpl<>(terminalResponseDtoList, pageable, totalRecords);

        return terminalResponsePage;
    }
}

package com.terminal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terminal.dtos.TerminalResponseDto;
import com.terminal.entities.Department;
import com.terminal.entities.Terminal;
import com.terminal.repositories.TerminalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ListTerminalsServiceTest {

    @Mock
    TerminalRepository terminalRepository;
    @InjectMocks
    ListTerminalsService underTestListTerminalsService;

    @Test
    void getActiveTerminalsPageWithTerminalName() throws JsonProcessingException {

        // given
        Department department = new Department();
        department.setDepartmentName("department");

        String terminalName = "terminal";
        Terminal terminal = new Terminal();
        terminal.setTerminalName(terminalName);
        terminal.setIsActive(true);
        terminal.setDepartmentName("department");
        terminal.setDepartment(department);
        List<Terminal> terminalList = List.of(terminal);
        department.setTerminalList(terminalList);

        int itemPerPage = 5;
        int pageNumber = 1;

        String sortDirectionAsc = "asc";
        String sortDirectionDesc = "desc";
        Pageable pageable = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by("id").ascending());
        Pageable pageable2 = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by("id").descending());

        // when
        when(terminalRepository.findByActiveTerminalsAndTerminalNameList(anyString())).thenReturn(terminalList);
        Page<TerminalResponseDto> terminalResponsePageListAsc = underTestListTerminalsService.getActiveTerminalsPage(pageNumber, sortDirectionAsc, terminalName);
        Page<TerminalResponseDto> terminalResponsePageListDesc = underTestListTerminalsService.getActiveTerminalsPage(pageNumber, sortDirectionDesc, terminalName);
        // then
        assertThat(terminalResponsePageListAsc.getTotalElements()).isEqualTo(terminalList.size());
        assertThat(terminalResponsePageListDesc.getTotalElements()).isEqualTo(terminalList.size());

    }

    @Test
    void getActiveTerminalsPageWithoutTerminalName() throws JsonProcessingException {
        // given
        Department department = new Department();
        department.setDepartmentName("department");

        Terminal terminal = new Terminal();
        terminal.setTerminalName("terminal");
        terminal.setIsActive(true);
        terminal.setDepartmentName("department");
        terminal.setDepartment(department);
        List<Terminal> terminalList = List.of(terminal);
        department.setTerminalList(terminalList);

        int itemPerPage = 5;
        int pageNumber = 1;

        String sortDirectionAsc = "asc";
        String sortDirectionDesc = "desc";
        Pageable pageable = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by("id").ascending());
        // when
        when(terminalRepository.findAllByActiveTerminalsList(pageable)).thenReturn(terminalList);
        Page<TerminalResponseDto> terminalResponsePageListAsc = underTestListTerminalsService.getActiveTerminalsPage(pageNumber, sortDirectionAsc);
        Page<TerminalResponseDto> terminalResponsePageListDesc = underTestListTerminalsService.getActiveTerminalsPage(pageNumber, sortDirectionDesc);
        // then
        assertThat(terminalResponsePageListAsc.getTotalElements()).isEqualTo(terminalList.size());
        assertThat(terminalResponsePageListDesc.getTotalElements()).isEqualTo(terminalList.size());
    }
}
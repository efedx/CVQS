package com.terminal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terminal.amqp.RabbitMQMessagePublisher;
import com.terminal.dtos.RegisterTerminalDto;
import com.terminal.entities.Department;
import com.terminal.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterTerminalsServiceTest {

    @Mock
    RabbitMQMessagePublisher rabbitMQMessagePublisher;
    @Mock
    DepartmentRepository departmentRepository;
    @InjectMocks
    RegisterTerminalsService underTestRegisterTerminalService;

    @Test
    void registerTerminals() throws JsonProcessingException {

        // when
        RegisterTerminalDto.TerminalDto terminalDto = new RegisterTerminalDto.TerminalDto();
        terminalDto.setTerminalName("terminal");
        terminalDto.setIsActive(true);
        List<RegisterTerminalDto.TerminalDto> terminalDtoList = List.of(terminalDto);

        RegisterTerminalDto registerTerminalDto = new RegisterTerminalDto();
        registerTerminalDto.setDepartmentName("department");
        registerTerminalDto.setTerminalList(terminalDtoList);
        List<RegisterTerminalDto> registerTerminalDtoList = List.of(registerTerminalDto);

        Department department = new Department();
        department.setDepartmentName("department");
        department.setTerminalList(underTestRegisterTerminalService.getTerminalListFromTerminalDtoList(department, terminalDtoList));

        // given
        underTestRegisterTerminalService.registerTerminals(registerTerminalDtoList);

        // then
        ArgumentCaptor<Department> departmentArgumentCaptor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepository).save(departmentArgumentCaptor.capture());

        Department capturedDepartment = departmentArgumentCaptor.getValue();

        assertThat(capturedDepartment.getDepartmentName()).isEqualTo(department.getDepartmentName());
        assertThat(capturedDepartment.getTerminalList().size()).isEqualTo(department.getTerminalList().size());
        assertThat(capturedDepartment.getTerminalList().get(0).getTerminalName()).isEqualTo(department.getTerminalList().get(0).getTerminalName());
        assertThat(capturedDepartment.getTerminalList().get(0).getIsActive()).isEqualTo(department.getTerminalList().get(0).getIsActive());
    }
}
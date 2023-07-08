package com.terminal.services;

import com.amqp.RabbitMQMessagePublisher;
import com.terminal.dtos.NotificationRequestDto;
import com.terminal.dtos.RegisterTerminalDto;
import com.terminal.models.Department;
import com.terminal.models.Terminal;
import com.terminal.repositories.DepartmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterTerminalsService implements com.terminal.interfaces.RegisterTerminalsService {

    private final RabbitMQMessagePublisher rabbitMQMessagePublisher;
    private final DepartmentRepository departmentRepository;

    //-----------------------------------------------------------------------------------------------

    /**
     * Registers terminals for multiple departments and saves them to the database.
     *
     * @param authorizationHeader     The authorization header containing the authentication token.
     * @param registerTerminalDtoList The list of RegisterTerminalDto objects containing the terminal details for each department.
     * @return The set of departments with the registered terminals.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    @Override
    public Set<Department> registerTerminals(String authorizationHeader, List<RegisterTerminalDto> registerTerminalDtoList) throws JsonProcessingException {

        Set<Department> departmentSet = new HashSet<>();

        for(RegisterTerminalDto registerTerminalDto: registerTerminalDtoList) {

            Department department = new Department();

            String departmentName = registerTerminalDto.getDepartmentName();
            department.setDepartmentName(departmentName);

            List<Terminal> terminalList = getTerminalListFromTerminalDtoList(department, registerTerminalDto.getTerminalList());
            department.setTerminalList(terminalList);

            departmentSet.add(departmentRepository.save(department));

            for (Terminal terminal: terminalList) {
                NotificationRequestDto notificationRequestDto = new NotificationRequestDto().builder()
                        .department(departmentName)
                        .terminal(terminal.getTerminalName())
                        .isActive(terminal.getIsActive())
                        .build();
                //amqpTemplate.convertAndSend("notification_terminal_exchange", "notification_terminal_routing_key", notificationRequestDto);
                rabbitMQMessagePublisher.publishNotificationTerminal(notificationRequestDto);
            }
        }
        return departmentSet;
    }

    //-----------------------------------------------------------------------------------------------

    private List<Terminal> getTerminalListFromTerminalDtoList(Department department, ArrayList<RegisterTerminalDto.TerminalDto> terminalDtoList) {

        List<Terminal> terminalList = new ArrayList<>();

        for(RegisterTerminalDto.TerminalDto terminalDto: terminalDtoList) {
            Terminal terminal = new Terminal(terminalDto.getTerminalName(), department.getDepartmentName(), terminalDto.getIsActive(), department);
            terminalList.add(terminal);
        }
        return terminalList;
    }

}

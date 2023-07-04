package com.employee.services;

import com.employee.dto.RegisterTerminalDto;
import com.employee.model.Department;
import com.employee.model.Terminal;
import com.employee.repository.DepartmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterTerminalsService implements com.employee.interfaces.RegisterTerminalsService {

    @Value("${url.security.terminals}")
    String securityTerminalsUrl;

    private final DepartmentRepository departmentRepository;
    private final SecurityContainerService securityContainerService;

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

        securityContainerService.jwtValidation(authorizationHeader, securityTerminalsUrl);

        Set<Department> departmentSet = new HashSet<>();

        for(RegisterTerminalDto registerTerminalDto: registerTerminalDtoList) {

            Department department = new Department();
            department.setDepartmentName(registerTerminalDto.getDepartmentName());
            department.setTerminalList(getTerminalListFromTerminalDtoList(department, registerTerminalDto.getTerminalList()));

            departmentSet.add(departmentRepository.save(department));
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

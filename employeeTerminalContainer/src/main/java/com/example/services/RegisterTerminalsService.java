package com.example.services;

import com.example.dto.RegisterTerminalDto;
import com.example.model.Department;
import com.example.model.Terminal;
import com.example.repository.DepartmentRepository;
import com.example.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterTerminalsService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public String registerTerminals(List<RegisterTerminalDto> registerTerminalDtoList) {

        for(RegisterTerminalDto registerTerminalDto: registerTerminalDtoList) {

            Department department = new Department();
            department.setDepartmentName(registerTerminalDto.getDepartmentName());
            department.setTerminalList(getTerminalListFromTerminalDtoList(department, registerTerminalDto.getTerminalList()));

            departmentRepository.save(department);
        }
        return "Terminals registered";
    }

    private List<Terminal> getTerminalListFromTerminalDtoList(Department department, ArrayList<RegisterTerminalDto.TerminalDto> terminalDtoList) {

        List<Terminal> terminalList = new ArrayList<>();

        for(RegisterTerminalDto.TerminalDto terminalDto: terminalDtoList) {
            Terminal terminal = new Terminal(terminalDto.getTerminalName(), department.getDepartmentName(), terminalDto.getIsActive(), department);
            terminalList.add(terminal);
        }
        return terminalList;
    }

}

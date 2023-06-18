package com.example.services;

import com.example.dto.JwtGenerationRequestDto;
import com.example.dto.RegisterTerminalDto;
import com.example.model.Department;
import com.example.model.Terminal;
import com.example.repository.DepartmentRepository;
import com.example.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterTerminalsService {
    private static final String securityTerminalsUrl = "http://security:8083/terminals";
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RestTemplate restTemplate;

    public Set<Department> registerTerminals(String authorizationHeader, List<RegisterTerminalDto> registerTerminalDtoList) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityTerminalsUrl, HttpMethod.POST, requestEntity, Object.class);

        Set<Department> departmentSet = new HashSet<>();

        for(RegisterTerminalDto registerTerminalDto: registerTerminalDtoList) {

            Department department = new Department();
            department.setDepartmentName(registerTerminalDto.getDepartmentName());
            department.setTerminalList(getTerminalListFromTerminalDtoList(department, registerTerminalDto.getTerminalList()));

            departmentSet.add(departmentRepository.save(department));
        }
        return departmentSet;
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

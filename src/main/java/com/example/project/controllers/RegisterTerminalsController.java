package com.example.project.controllers;

import com.example.project.dto.RegisterDefectDto;
import com.example.project.dto.RegisterTerminalDto;
import com.example.project.services.RegisterTerminalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "registerTerminals")
@RequiredArgsConstructor
public class RegisterTerminalsController {

    @Autowired
    RegisterTerminalsService registerTerminalsService;

    @PostMapping("registerTerminals")
    public String registerDefects(@RequestBody List<RegisterTerminalDto> registerTerminalDtoList) {
        return registerTerminalsService.registerTerminals(registerTerminalDtoList);
    }

}

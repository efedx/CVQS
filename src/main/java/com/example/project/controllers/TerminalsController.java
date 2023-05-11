package com.example.project.controllers;

import com.example.project.dto.RegisterTerminalDto;
import com.example.project.dto.TerminalResponseDto;
import com.example.project.model.Terminal;
import com.example.project.services.ListTerminalsService;
import com.example.project.services.RegisterTerminalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/terminals")
@RequiredArgsConstructor
public class TerminalsController {

    @Autowired
    RegisterTerminalsService registerTerminalsService;
    @Autowired
    ListTerminalsService listTerminalsService;

    @PostMapping("registerTerminals")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerDefects(@RequestBody List<RegisterTerminalDto> registerTerminalDtoList) {
        return registerTerminalsService.registerTerminals(registerTerminalDtoList);
    }

    @GetMapping("listTerminals/page/{pageNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TerminalResponseDto>> getTerminalsPage(@PathVariable int pageNumber, @RequestParam String sortDirection,
                                                                      @RequestParam(required = false) String terminalName) {
        if(terminalName != null) {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(pageNumber, sortDirection, terminalName));
        }
        else {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(pageNumber, sortDirection));
        }
    }

}

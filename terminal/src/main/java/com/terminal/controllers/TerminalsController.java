package com.terminal.controllers;

import com.terminal.dtos.RegisterTerminalDto;
import com.terminal.dtos.TerminalResponseDto;
import com.terminal.services.ListTerminalsService;
import com.terminal.services.RegisterTerminalsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/terminals")
@RequiredArgsConstructor
public class TerminalsController {

    private static final Logger logger = LogManager.getLogger(TerminalsController.class);
    private final RegisterTerminalsService registerTerminalsService;
    private final ListTerminalsService listTerminalsService;

    //-----------------------------------------------------------------------------------------------

    @PostMapping("/registerTerminals")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerTerminals(@RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestBody List<RegisterTerminalDto> registerTerminalDtoList) throws JsonProcessingException {
        registerTerminalsService.registerTerminals(authorizationHeader, registerTerminalDtoList);
        return ResponseEntity.ok().body("Terminals registered");
    }

    @GetMapping("/listTerminals/page/{pageNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TerminalResponseDto>> getTerminalsPage(@RequestHeader("Authorization") String authorizationHeader,
                                                                      @PathVariable int pageNumber,
                                                                      @RequestParam String sortDirection,
                                                                      @RequestParam(required = false) String terminalName) throws JsonProcessingException {
        if(terminalName != null) {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(authorizationHeader, pageNumber, sortDirection, terminalName));
        }
        else {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(authorizationHeader, pageNumber, sortDirection));
        }
    }

}

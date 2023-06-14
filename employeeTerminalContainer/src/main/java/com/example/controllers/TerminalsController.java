package com.example.controllers;

import com.example.dto.RegisterTerminalDto;
import com.example.dto.TerminalResponseDto;
import com.example.model.Terminal;
import com.example.services.ListTerminalsService;
import com.example.services.RegisterTerminalsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(TerminalsController.class);

    @Autowired
    RegisterTerminalsService registerTerminalsService;
    @Autowired
    ListTerminalsService listTerminalsService;

    @PostMapping("/registerTerminals")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerTerminals(@RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestBody List<RegisterTerminalDto> registerTerminalDtoList) {
        registerTerminalsService.registerTerminals(authorizationHeader, registerTerminalDtoList);
        return ResponseEntity.ok().body("Terminals registered");
    }

    @GetMapping("/listTerminals/page/{pageNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TerminalResponseDto>> getTerminalsPage(@RequestHeader("Authorization") String authorizationHeader,
                                                                      @PathVariable int pageNumber,
                                                                      @RequestParam String sortDirection,
                                                                      @RequestParam(required = false) String terminalName) {
        if(terminalName != null) {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(authorizationHeader, pageNumber, sortDirection, terminalName));
        }
        else {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(authorizationHeader, pageNumber, sortDirection));
        }
    }

}

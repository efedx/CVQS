package com.example.project.controllers;

import com.example.project.model.Department;
import com.example.project.model.Terminal;
import com.example.project.services.ListDefectsService;
import com.example.project.services.ListTerminalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListTerminalsController {

    @Autowired
    ListTerminalsService listTerminalsService;

    @GetMapping("listTerminals/page/{pageNumber}")
    public ResponseEntity<Page<Terminal>> getTerminalsPage(@PathVariable int pageNumber, @RequestParam String sortDirection,
                                                             @RequestParam(required = false) String terminalName) {
        if(terminalName != null) {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(pageNumber, sortDirection, terminalName));
        }
        else {
            return ResponseEntity.ok().body(listTerminalsService.getActiveTerminalsPage(pageNumber, sortDirection));
        }
    }
}

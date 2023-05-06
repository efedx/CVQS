package com.example.project.controllers;

import com.example.project.dto.LogDefectDto;
import com.example.project.services.LogDefectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "defectLog")
@RequiredArgsConstructor
public class LogDefectsController {
    @Autowired
    LogDefectsService logDefectsService;

    @PostMapping("logDefects")
    public String logDefects(@RequestBody LogDefectDto logDefectDto) {
        return logDefectsService.logDefects(logDefectDto);
    }
}

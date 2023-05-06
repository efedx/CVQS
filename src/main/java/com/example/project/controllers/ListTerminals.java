package com.example.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListTerminals {
    @GetMapping("listTerminals")
    public String listTerminals() {
        return "it's done";
    }
}

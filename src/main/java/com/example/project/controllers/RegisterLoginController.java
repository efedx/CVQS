package com.example.project.controllers;

import com.example.project.dto.LoginRequestDto;
import com.example.project.dto.AuthenticationResponseDto;
import com.example.project.dto.RegisterRequestDto;
import com.example.project.services.RegisterLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterLoginController {

    @Autowired
    private final RegisterLoginService registerLoginService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(registerLoginService.register(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(registerLoginService.login(loginRequestDto));
    }

}

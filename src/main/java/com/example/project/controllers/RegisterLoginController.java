package com.example.project.controllers;

import com.example.project.dto.AuthenticationRequestDto;
import com.example.project.security.AuthenticationResponse;
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(registerLoginService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(registerLoginService.login(request));
    }

}

package com.example.controllers;

import com.example.dto.LoginRequestDto;
import com.example.services.JwtValidaitonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class JwtValidationController {

    @Autowired
    JwtValidaitonService jwtValidaitonService;

    @PostMapping("/isTokenValid") // todo only admins can do that
    public ResponseEntity<Boolean> isTokenValid(@RequestHeader String jwt) {
        return ResponseEntity.ok(jwtValidaitonService.isTokenValid(jwt));
    }

}

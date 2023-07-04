package com.security.controllers;

import com.security.dto.JwtDto;
import com.security.dto.LoginRequestDto;
import com.security.services.JwtValidationService;
import com.security.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final LoginService loginService;
    private final JwtValidationService jwtValidationService;

    //-----------------------------------------------------------------------------------------------

    @PostMapping("/login") // NO AUTHORIZATION
    public ResponseEntity<JwtDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(JwtDto.builder().username(loginRequestDto.getUsername()).token(loginService.login(loginRequestDto)).build());
    }

    @PostMapping("/userManagement") // ADMIN
    public ResponseEntity<Boolean> userManagement(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/defects") // OPERATOR
    public ResponseEntity<Boolean> defects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/listDefects") // LEADER
    public ResponseEntity<Boolean> listDefects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }

    @PostMapping("/terminals") // ALL
    public ResponseEntity<Boolean> terminals(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(authorizationHeader));
    }
}

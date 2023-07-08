package com.security.controllers;

import com.common.JwtDto;
import com.common.LoginRequestDto;
import com.security.services.JwtValidationService;
import com.security.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(true);
    }

    @PostMapping("/registerDefects") // OPERATOR
    public ResponseEntity<Boolean> defects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(true);
    }

    @PostMapping("/defects") // LEADER
    public ResponseEntity<Boolean> listDefects(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(true);
    }

    @PostMapping("/terminals") // ALL
    public ResponseEntity<Boolean> terminals(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(true);
    }
}

package com.security.controllers;

import com.security.services.JwtValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtValidationController {

    private final JwtValidationService jwtValidationService;

    //-----------------------------------------------------------------------------------------------

    @PostMapping("/isTokenValid") // todo only admins can do that
    public ResponseEntity<Boolean> isTokenValid(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(jwtValidationService.isTokenValid(jwt));
    }

}

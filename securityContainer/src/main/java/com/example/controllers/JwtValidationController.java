package com.example.controllers;

import com.example.services.JwtValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

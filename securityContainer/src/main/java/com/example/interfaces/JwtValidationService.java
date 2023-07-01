package com.example.interfaces;

public interface JwtValidationService {
    boolean isTokenValid(String authorizationHeader);
}

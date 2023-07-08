package com.security.interfaces;

public interface JwtValidationService {
    boolean isTokenValid(String authorizationHeader);
}

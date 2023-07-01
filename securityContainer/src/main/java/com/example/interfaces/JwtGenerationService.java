package com.example.interfaces;

import com.example.model.Roles;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface JwtGenerationService {
    String generateJwt(String username, Set<Roles> rolesSet);
}

package com.security.interfaces;

import com.security.entities.Roles;

import java.util.Set;

public interface JwtGenerationService {
    String generateJwt(String username, Set<Roles> rolesSet);
}

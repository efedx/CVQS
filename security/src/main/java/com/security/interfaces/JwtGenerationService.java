package com.security.interfaces;

import com.security.model.Roles;

import java.util.Set;

public interface JwtGenerationService {
    String generateJwt(String username, Set<Roles> rolesSet);
}

package com.example.project.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

import static com.example.project.security.SecurityConstans.JWT_KEY;

@Service
@RequiredArgsConstructor
public class JwtGenerationService {

    Date now = new Date();
    Date expiration = new Date(now.getTime() + 360000000);

    // used for authentication
    public String generateJwt(Authentication authentication) {
        return Jwts.builder()
                .setIssuer("Toyota Project")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("username", authentication.getName())
                .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    // used for user details

    public String generateJwt(UserDetails userDetails) {
        return generateJwt(new HashMap<>(), userDetails);
    }

    public String generateJwt(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setIssuer("Toyota Project")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setClaims(extraClaims)
                .claim("username", userDetails.getUsername())
                .claim("authorities", populateAuthorities(userDetails.getAuthorities()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>(); // set is an interface, hashset is an implementation
        for(GrantedAuthority authority: collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}

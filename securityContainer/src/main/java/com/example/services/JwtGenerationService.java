package com.example.services;

import com.example.model.Roles;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

import static com.example.security.SecurityConstans.JWT_KEY;

@Service
@RequiredArgsConstructor
public class JwtGenerationService {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + 999999999); // 360000000

    // used for authentication
    public String generateJwt(Authentication authentication) {
        return Jwts.builder()
                .setIssuer("Toyota Project")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("username", authentication.getName())
                .claim("authorities", grantedAuthoritiesToString(authentication.getAuthorities()))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    // used for user details

//    public String generateJwt(UserDetails userDetails) {
//        return generateJwt(new HashMap<>(), userDetails);
//    }
//
//    public String generateJwt(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                .setIssuer("Toyota Project")
//                .setIssuedAt(now)
//                .setExpiration(expiration)
//                .setClaims(extraClaims)
//                .claim("username", userDetails.getUsername())
//                .claim("authorities", populateAuthorities(userDetails.getAuthorities()))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }

    public String generateJwt(String username, Set<Roles> rolesSet) {
        return Jwts.builder()
                .setIssuer("Toyota Project")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("username", username)
                //.claim("authorities", roles)
                //.claim("authorities", grantedAuthoritiesToString(customUserDetailsService.getSimpleGrantedAuthorities(roles)))
                .claim("authorities", rolesSetToString(rolesSet))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String grantedAuthoritiesToString (Collection<? extends GrantedAuthority> grantedAuthorityCollection) {
        Set<String> authoritiesStringSet = new HashSet<>(); // set is an interface, hashset is an implementation
        for(GrantedAuthority authority: grantedAuthorityCollection) {
            authoritiesStringSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesStringSet);
    }

    private String rolesSetToString (Set<Roles> rolesSet) {
        Set<String> roleStringSet = new HashSet<>(); // set is an interface, hashset is an implementation
        for(Roles role: rolesSet) {
            roleStringSet.add(role.getRoleName());
        }
        return String.join(",", roleStringSet);
    }
}

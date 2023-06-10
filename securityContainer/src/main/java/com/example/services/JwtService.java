package com.example.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.example.security.SecurityConstans.JWT_KEY;

@Service
@RequiredArgsConstructor
public class JwtService {

    //----------------------------------------------

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String tokenUsername = extractUsername(jwtToken);

        return (tokenUsername.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    //----------------------------------------------
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Claims getAllClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }
    private SecretKey getSigningKey() { // todo you used multiple times this function
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

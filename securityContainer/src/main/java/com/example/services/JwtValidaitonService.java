package com.example.services;

import com.example.dto.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.net.http.HttpRequest;

import static com.example.security.SecurityConstans.JWT_KEY;

@Service
@RequiredArgsConstructor
public class JwtValidaitonService {

    public boolean isTokenValid(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }

        final String jwt = authorizationHeader.substring(7);

        if(jwt != null) { // todo jwt.isEmpty()
            try {
                // does the validation and returns the claims
                Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();

                // get the username and authorities to create a UPA token and set authentication in the security context
                String username = String.valueOf(claims.get("username"));

                String authorities = String.valueOf(claims.get("authorities")); // (String) claims.get("authorities")
//                String realRoles = parseRoles(authorities);

                UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
//                AuthorityUtils.createAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(upaToken);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid Token Received");
            }
        }
        return true;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

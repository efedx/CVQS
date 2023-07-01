package com.example.services;

import com.example.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtValidationService implements com.example.interfaces.JwtValidationService {

    private final SecretKey signingKey;

    //-----------------------------------------------------------------------------------------------

    /**
     * Checks if the provided authorization token is a valid token.
     *
     * @param authorizationHeader The authorization header containing the token. It should be in the format "Bearer <token>".
     * @return {@code true} if the token is valid, {@code false} otherwise.
     * @throws InvalidTokenException If the token is invalid or cannot be parsed.
     */
    @Override
    public boolean isTokenValid(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }

        final String jwt = authorizationHeader.substring(7);

        if(jwt != null) { // todo jwt.isEmpty()
            try {
                // does the validation and returns the claims
                Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(jwt).getBody();

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
                throw new InvalidTokenException("Invalid token received");
            }
        }
        return true;
    }
}

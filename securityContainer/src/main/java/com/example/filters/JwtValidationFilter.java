package com.example.filters;


import com.example.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.security.SecurityConstans.JWT_HEADER;
import static com.example.security.SecurityConstans.JWT_KEY;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);

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
                throw new InvalidTokenException("Invalid token received");
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // do not do the filter when it is "/login"
//        return request.getServletPath().equals("/login");
        List<String> excludedUrls = Arrays.asList("/registerAdmin", "/login", "/test");
        String requestUrl = request.getServletPath();
        return excludedUrls.stream().anyMatch(url -> requestUrl.matches(url));
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    private String parseRoles(String keyValue) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(keyValue);
//    }
}

package com.example.project.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

import static com.example.project.security.SecurityConstans.JWT_HEADER;
import static com.example.project.security.SecurityConstans.JWT_KEY;

public class JwtValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String jwt = request.getHeader(JWT_HEADER);

        if(jwt != null) { // todo jwt.isEmpty()
            try {
                Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();

                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");
                UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(upaToken);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid Token Received");
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login");
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

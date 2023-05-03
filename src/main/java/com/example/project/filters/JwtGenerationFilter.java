package com.example.project.filters;

import com.example.project.services.JwtGenerationService;
import com.example.project.services.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

import static com.example.project.security.SecurityConstans.JWT_HEADER;
import static com.example.project.security.SecurityConstans.JWT_KEY;

@Component
@RequiredArgsConstructor
public class JwtGenerationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtGenerationService jwtGenerationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            // generate a jwt based on the authentication object of the security context and set the header with it
            String jwt = jwtGenerationService.generateJwt(authentication);
            response.setHeader("Authorization", jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // do the filter only for "/login"
        return !request.getServletPath().equals("/login"); // return true to avoid
    }

}

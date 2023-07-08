package com.security.filters;


import com.security.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final SecretKey signingKey;

    //-----------------------------------------------------------------------------------------------

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
                Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(jwt).getBody();

                // get the username and authorities to create a UPA token and set authentication in the security context
                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));

                UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                // set the security context, so that username password authentication filter becomes necessary
                SecurityContextHolder.getContext().setAuthentication(upaToken);
            }
            catch (Exception e) {
                throw new InvalidTokenException("Invalid token received");
            }
        }

        filterChain.doFilter(request, response);
    }

    //-----------------------------------------------------------------------------------------------

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // do not do the filter when it is "/login"
        List<String> excludedUrls = List.of("/login");
        String requestUrl = request.getServletPath();
        return excludedUrls.stream().anyMatch(url -> requestUrl.matches(url));
    }
}

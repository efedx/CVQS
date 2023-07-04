package com.security.security;

import com.security.exceptions.CustomAccessDeniedHandler;
import com.security.exceptions.CustomAuthenticationEntryPoint;
import com.security.filters.ExceptionHandlerFilter;
import com.security.filters.JwtValidationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityFilterChain {

    private final SecretKey signingKey;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/login").permitAll() // white list that does not require authentication
                .requestMatchers("/userManagement").hasAuthority("ADMIN")
                .requestMatchers("/defects").hasAuthority("OPERATOR")
                .requestMatchers("/listDefects").hasAuthority("LEADER")
                .anyRequest().authenticated(); // but any other url must be authenticated

        http.exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // do not produce JSessionIDs and HTTP sessions

        http.authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(new JwtValidationFilter(signingKey), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtValidationFilter.class);

        return http.build();
    }
}

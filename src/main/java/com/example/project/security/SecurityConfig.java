package com.example.project.security;

import com.example.project.filters.JwtAuthenticationFilter;
import com.example.project.filters.JwtGenerationFilter;
import com.example.project.filters.JwtValidationFilter;
import com.example.project.repository.EmployeeRepository;
import com.example.project.services.JwtGenerationService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;

import static com.example.project.security.SecurityConstans.JWT_KEY;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final EmployeeRepository employeeRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeHttpRequests()
                .requestMatchers("/register", "/login").permitAll() // white list that does not require authentication
                .anyRequest().authenticated(); // but any other url must be authenticated

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // do not produce JSessionIDs and HTTP sessions

        http.authenticationProvider(authenticationProvider)
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtGenerationFilter(new JwtGenerationService()), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

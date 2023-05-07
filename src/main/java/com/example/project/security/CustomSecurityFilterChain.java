package com.example.project.security;

import com.example.project.filters.JwtGenerationFilter;
import com.example.project.filters.JwtValidationFilter;
import com.example.project.services.JwtGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityFilterChain {

    @Autowired
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private final JwtGenerationService jwtGenerationService;

    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/register", "/login").permitAll() // white list that does not require authentication
                .requestMatchers("/userManagement/**").hasAuthority("ADMIN")
                .requestMatchers("/listDefects/**").hasAuthority("LEADER")
                .requestMatchers("/registerDefects").hasAuthority("OPERATOR")
                .anyRequest().authenticated(); // but any other url must be authenticated

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // do not produce JSessionIDs and HTTP sessions

        http.authenticationProvider(customAuthenticationProvider)
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtGenerationFilter(jwtGenerationService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtValidationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public CustomAuthenticationProvider customAuthenticationProvider() {
//        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
//        return customAuthenticationProvider;
//    }



}

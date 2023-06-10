package com.example.security;

import com.example.filters.JwtValidationFilter;
import com.example.services.JwtGenerationService;
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
                .requestMatchers("/registerAdmin", "/login").permitAll() // white list that does not require authentication
                .requestMatchers("/userManagement").hasAuthority("ADMIN")
                .requestMatchers("/defects").hasAuthority("OPERATOR")
                .requestMatchers("/listDefects").hasAuthority("LEADER")
                .anyRequest().authenticated(); // but any other url must be authenticated


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // do not produce JSessionIDs and HTTP sessions

        http.authenticationProvider(customAuthenticationProvider)
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtValidationFilter(), UsernamePasswordAuthenticationFilter.class) // todo new JwtValidationFilter(jwtGenerationService)
                .addFilterBefore(new JwtValidationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public CustomAuthenticationProvider customAuthenticationProvider() {
//        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
//        return customAuthenticationProvider;
//    }



}

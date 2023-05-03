package com.example.project.config;

import com.example.project.model.Employee;
import com.example.project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.example.project.security.Role.ADMIN;
import static com.example.project.security.Role.USER;

@Configuration
@RequiredArgsConstructor
public class EmployeeConfig {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository) {
        return args -> {
            Employee one = new Employee("one", "one@mail.com", passwordEncoder.encode("123"), ADMIN);
            Employee two = new Employee("two", "two@mail.com", passwordEncoder.encode("456"), USER);
            employeeRepository.saveAll(List.of(one, two));
        };
    }
}
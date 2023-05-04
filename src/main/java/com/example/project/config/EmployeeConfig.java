package com.example.project.config;

import com.example.project.model.Employee;
import com.example.project.repository.EmployeeRepository;
import com.example.project.security.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class EmployeeConfig {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository) {
        return args -> {
            Employee one = new Employee("one", "one@mail.com", passwordEncoder.encode("123"), RoleEnum.valueOf("TEAM_LEADER"));
            Employee two = new Employee("two", "two@mail.com", passwordEncoder.encode("456"), RoleEnum.valueOf("ADMIN"));
            employeeRepository.saveAll(List.of(one, two));
        };
    }
}
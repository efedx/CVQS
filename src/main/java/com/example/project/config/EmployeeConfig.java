package com.example.project.config;

import com.example.project.model.Employee;
import com.example.project.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.example.project.security.Role.ADMIN;
import static com.example.project.security.Role.USER;

@Configuration
public class EmployeeConfig {


    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository) {
        return args -> {
            Employee one = new Employee("one", "one@mail.com", "123", ADMIN);
            Employee two = new Employee("two", "two@mail.com", "456", USER);
            employeeRepository.saveAll(List.of(one, two));
        };
    }
}
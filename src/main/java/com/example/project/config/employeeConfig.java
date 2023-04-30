package com.example.project.config;

import com.example.project.model.Employee;
import com.example.project.repository.EmployeeRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class employeeConfig {

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepo employeeRepo) {
        return args -> {
            Employee one = new Employee("one", "one@mail.com");
            Employee two = new Employee("two", "two@mail.com");
            employeeRepo.saveAll(List.of(one, two));
        };
    }

}
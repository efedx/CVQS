package com.example.project.config;

import com.example.project.model.*;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.RolesRepository;
import com.example.project.repository.VehicleRepository;
import com.example.project.security.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Array;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class EmployeeConfig {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    // add these roles to roles table
    Roles admin = new Roles("ADMIN");
    Roles operator = new Roles("OPERATOR");
    Roles leader = new Roles("LEADER");
    Set<Roles> adminLeader = new HashSet<>(Arrays.asList(admin, operator));
    Set<Roles> adminLeaderOperator = new HashSet<>(Arrays.asList(admin, operator, leader));

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository, RolesRepository rolesRepository, VehicleRepository vehicleRepository) {
        return args -> {
            rolesRepository.saveAll(List.of(admin, operator, leader));

            Employee one = new Employee("one", "one@mail.com", passwordEncoder.encode("123"), adminLeader);
            Employee two = new Employee("two", "two@mail.com", passwordEncoder.encode("456"), adminLeaderOperator);
            //employeeRepository.saveAll(List.of(one, two));

//            Vehicle vehicle = new Vehicle((long)1, new ArrayList<Defect>());
//            vehicleRepository.save(vehicle);
        };
    }
}
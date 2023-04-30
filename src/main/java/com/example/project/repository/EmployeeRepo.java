package com.example.project.repository;

import com.example.project.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findByEmail(String email);
    //Optional<List<Employee>> findByDeletedTrue();

    //you can use queries here
    List<Employee> findByDeletedTrue();
    List<Employee> findByDeletedFalse();
}

package com.example.repository;

import com.example.model.Employee;
import com.example.model.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findByEmail(String email);
    //Optional<List<Employee>> findByDeletedTrue();

    //you can use queries here
    List<Employee> findByDeletedTrue();
    List<Employee> findByDeletedFalse();

    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE id =?1")
    void setDeletedTrue(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.username = COALESCE(:username, e.username), e.password = COALESCE(:password, e.password)," +
            " e.email = COALESCE(:email, e.email), e.roles = COALESCE(:roles, e.roles) WHERE e.id = :id") // e.roles = COALESCE(:roles, e.roles) todo impelent roles
    void updateEmployeeById(Long id, String username, String password, String email, Set<Roles> roles);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.username = COALESCE(:username, e.username), e.password = COALESCE(:password, e.password)," +
            " e.email = COALESCE(:email, e.email) WHERE e.id = :id")
    void updateEmployeeById(Long id, String username, String password, String email);
}

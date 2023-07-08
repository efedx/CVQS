package com.employee.repository;

import com.common.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE id =?1")
    void setDeletedTrue(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.username = COALESCE(:username, e.username), e.password = COALESCE(:password, e.password)," +
            " e.email = COALESCE(:email, e.email) WHERE e.id = :id")
    void updateEmployeeById(Long id, String username, String password, String email);
}

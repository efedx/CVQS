package com.example.project.repository;

import com.example.project.model.Employee;
import com.example.project.model.Roles;
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
    @Query("UPDATE Employee e SET e.username = ?1, e.password = ?2, e.email = ?3, e.roles = ?4 WHERE e.id = ?5")
    void updateEmployeeById(String username, String password, String email, Set<Roles> roles, Long id);

    @Modifying
    @Query("UPDATE Employee e SET e.username = ?1, e.password = ?2, e.email = ?3 WHERE e.id = ?4")
    void updateWithoutRoles(String username, String password, String email, Long id);

    @Modifying
    @Query("UPDATE Employee e SET e.roles = ?1 WHERE e.id = ?2")
    void updateRolesById(Set<Roles> roles, Long id);

    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE id =?1")
    void setDeletedTrue(Long id);
}

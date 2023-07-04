package com.employee.repository;

import com.employee.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    @Modifying
    @Query("DELETE Roles r WHERE r.employee.id= :id")
    void deleteRoleById(Long id);
}

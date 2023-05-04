package com.example.project.repository;

import com.example.project.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    public Roles findByRoleName(String roleName);
}

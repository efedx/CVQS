package com.example.repository;

import com.example.model.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    public Roles findByRoleName(String roleName);

//    @Modifying
//    @Transactional
//    @Query("UPDATE v FROM Vehicle v LEFT JOIN v.defectList d WHERE d.defectName = :defectName")
//    @Query("UPDATE Roles r SET e.roles = :roles WHERE e.id = :id") // e.roles = COALESCE(:roles, e.roles)
//    void putRoles(Long id, Set<Roles> roles);

}

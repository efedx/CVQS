package com.security.repository;

import com.security.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    public Roles findByRoleName(String roleName);

//    @Modifying
//    @Transactional
//    @Query("UPDATE v FROM Vehicle v LEFT JOIN v.defectList d WHERE d.defectName = :defectName")
//    @Query("UPDATE Roles r SET e.roles = :roles WHERE e.id = :id") // e.roles = COALESCE(:roles, e.roles)
//    void putRoles(Long id, Set<Roles> roles);

}

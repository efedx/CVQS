package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;


import javax.management.relation.Role;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Boolean.FALSE;
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "employees")
//@SQLDelete(sql = "UPDATE employees SET deleted = true WHERE id=?")
//@Where(clause = "deleted = false")
@FilterDef(name = "deletedEmployeeFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedEmployeeFilter", condition = "deleted = :isDeleted")

public class Employee extends Id {
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", targetEntity = Roles.class, cascade = CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference("employee_roles")
    @ElementCollection
    private Set<Roles> roles = new HashSet<>();
    private Boolean deleted;


    public Employee(String username, String email, String password, Set<Roles> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

//    public void addRoleSet(Set<Roles> roleSet) {
//        this.roles.addAll(roleSet);
//    }

    @PrePersist
    void preInsert() {
        if(this.deleted == null) {
            this.deleted = FALSE;
        }
//        if(this.roles == null || (this.roles.size() == 0)) {
//            Set<Roles> rolesSet = new HashSet<>();
//            rolesSet.add(new Roles("ADMIN"));
//            this.roles = rolesSet;
//        }
    }


    //-------------------------------------------------------------

    // since only the email will be unique we need it as the username But not here
}
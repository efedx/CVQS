package com.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;


import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.FALSE;
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
//@SQLDelete(sql = "UPDATE employees SET deleted = true WHERE id=?")
//@Where(clause = "deleted = false")
@FilterDef(name = "deletedEmployeeFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedEmployeeFilter", condition = "deleted = :isDeleted")

public class Employee extends com.security.model.Id {
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
//  @OneToMany(mappedBy = "id", targetEntity = Roles.class, cascade = CascadeType.ALL, orphanRemoval=true)
    @OneToMany(mappedBy = "employee", targetEntity = Roles.class, cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
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

    @PrePersist
    void preInsert() {
        if(this.deleted == null) {
            this.deleted = FALSE;
        }
    }
}
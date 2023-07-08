package com.common;

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
import static java.lang.Boolean.TRUE;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "employee")
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
    @OneToMany(mappedBy = "employee", targetEntity = Roles.class, cascade = CascadeType.ALL, orphanRemoval=false, fetch = FetchType.LAZY)
    //@JoinColumn(name = "employee")
    @JsonManagedReference("employee_roles")
    @ElementCollection
    private Set<Roles> roles = new HashSet<>();
    private Boolean deleted;
    private String notification;
    private String department;
    private String terminal;

    public void updateRoles(Set<Roles> rolesSet) {
            this.setRoles(rolesSet);
    }


    public Employee(String username, String email, String password, Set<Roles> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Employee(String username, String email, String password, Set<Roles> roles, String department, String terminal) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.department = department;
        this.terminal = terminal;
    }

    @PrePersist
    void preInsert() {
        if(this.deleted == null) {
            this.deleted = FALSE;
        }
    }
}
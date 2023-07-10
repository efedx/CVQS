package com.notification.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "employees")
@FilterDef(name = "deletedEmployeeFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedEmployeeFilter", condition = "deleted = :isDeleted")
public class Employee extends Id {
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", targetEntity = Roles.class, cascade = CascadeType.ALL, orphanRemoval=false, fetch = FetchType.LAZY)
    @JsonManagedReference("employee_roles")
    @ElementCollection
    private Set<Roles> roles = new HashSet<>();
    private Boolean deleted;
    private String notification;
    private String department;
    private String terminal;

}
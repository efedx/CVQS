package com.example.project.model;

import com.example.project.security.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Roles extends Id{

//    @Column(name = "roleName", unique = true, nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private RoleEnum roleName;

    private String roleName;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "roles") // consider cascade type
    private Set<Employee> employees = new HashSet<>();

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Employee.class)
//    @JoinColumn(name = "customerId")
//    private Set<Employee> employees = new HashSet<>();

    public Roles(String roleName) {
        this.roleName = roleName;
    }
}

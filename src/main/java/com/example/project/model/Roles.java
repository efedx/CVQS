package com.example.project.model;

import com.example.project.security.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long roleId;

    @Column(name = "roleName", nullable = false) // unique = true,
    @Enumerated(value = EnumType.STRING)
    private RoleEnum roleName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "roles")
    private Set<Employee> employees = new HashSet<>();

    public Roles(Set<String> ) {

    }
}

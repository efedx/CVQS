package com.security.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Roles extends com.security.entities.Id {

    private String roleName;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id") // , referencedColumnName = "id"
    //@ElementCollection
    @JsonBackReference("employee_roles")
    private Employee employee;

    public Roles(Employee employee, String roleName) {
        this.employee = employee;
        this.roleName = roleName;
    }
    public Roles(String roleName) {
        this.roleName= roleName;
    }
}

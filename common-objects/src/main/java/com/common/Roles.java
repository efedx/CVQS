package com.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Builder
public class Roles extends Id {
    @Override
    public String toString() {
        return "Roles{" +
                "roleName='" + roleName + '\'' +
                '}';
    }

    private String roleName;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @ElementCollection
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

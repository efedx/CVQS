package com.example.project.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

@Data
@Entity
@Table(name = "employees")
@SQLDelete(sql = "UPDATE employees SET deleted = true WHERE id=?")
//@Where(clause = "deleted = false")
@FilterDef(name = "deletedEmplooyeeFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedEmplooyeeFilter", condition = "deleted = :isDeleted")

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    //private String password;
    //private String role;

    private Boolean deleted = Boolean.FALSE;

    public Employee() {}
    public Employee(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

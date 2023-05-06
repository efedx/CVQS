package com.example.project.model;

import com.example.project.security.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

public class Employee extends Id{
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // consider cascade type
    @NotEmpty
    @JoinTable(
            name = "employee_roles",
            joinColumns = {
                @JoinColumn(name = "employee_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "role_name", referencedColumnName = "roleName")})
    private Set<Roles> roles = new HashSet<>();

//    @JsonIgnore
//    @OneToMany(mappedBy = "employees", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
//    private Set<Roles> roles = new HashSet<>();
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
//        if(this.roles == null || (this.roles.size() == 0)) {
//            Set<Roles> rolesSet = new HashSet<>();
//            rolesSet.add(new Roles("ADMIN"));
//            this.roles = rolesSet;
//        }
    }


    //-------------------------------------------------------------

    // since only the email will be unique we need it as the username But not here
}
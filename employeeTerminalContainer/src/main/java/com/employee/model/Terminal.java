package com.employee.model;

import com.common.Id;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import static java.lang.Boolean.FALSE;

@Entity
@Table(name = "terminals")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Terminal extends Id {

    private String terminalName;
    private String departmentName;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference("department-terminal")
    Department department;

    @PrePersist
    void preInsert() {
        if (this.isActive == null) {
            this.isActive = FALSE;
        }
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "terminalName='" + terminalName + '\'' +
                '}';
    }
}

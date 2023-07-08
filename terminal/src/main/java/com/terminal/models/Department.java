package com.terminal.models;

import com.common.Id;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department extends Id {

    private String departmentName;

    @OneToMany(targetEntity = Terminal.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ElementCollection
    @JsonManagedReference("department-terminal")
    private List<Terminal> terminalList = new ArrayList<>();;

}

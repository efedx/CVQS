package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends Id {

    private Long vehicleNo;

    @OneToMany(targetEntity = Defect.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ElementCollection
    @JsonManagedReference("vehicle_defect")
    private List<Defect> defectList = new ArrayList<>();

    public Vehicle(Long vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}

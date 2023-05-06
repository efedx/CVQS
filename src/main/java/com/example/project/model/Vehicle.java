package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(targetEntity = Defect.class, cascade = CascadeType.ALL)
    @JsonManagedReference("vehicle-defect")
    private ArrayList<Defect> defectList = new ArrayList<>();

    public Vehicle(Long vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}

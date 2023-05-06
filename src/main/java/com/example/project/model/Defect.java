package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Defect extends Id {

    private String defectName;

    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL)
    @JsonManagedReference("defect_location")
    private ArrayList<Location> locationList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference("vehicle_defect")
    Vehicle vehicle;

    public Defect(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}

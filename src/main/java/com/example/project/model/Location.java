package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location extends Id {

    @ManyToOne
    @JoinColumn(name = "defect_id")
    @JsonBackReference("defect_location")
    Defect defect;

    private int[] location;

    public Location(int[] location) {
        this.location = location;
    }
}

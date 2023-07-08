package com.defect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Embeddable
public class Location extends Id {

    @ManyToOne
    @JoinColumn(name = "defect_id")
    @JsonBackReference("defect_location")
    Defect defect;

    @ElementCollection
    private List<Integer> location = new ArrayList<>();

    public Location(ArrayList<Integer> location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Location{" +
                "location=" + location +
                '}';
    }
}

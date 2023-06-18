package com.example.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import java.sql.Blob;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Embeddable
public class Defect extends Id {

    private String defectName;

    @Override
    public String toString() {
        return "Defect{" +
                "defectName='" + defectName + '\'' +
                ", locationList=" + locationList.toString() +
                '}';
    }

    @JsonIgnore
    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL)
    @JsonManagedReference("defect_location")
    private List<Location> locationList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference("vehicle_defect")
    Vehicle vehicle;

    @Lob
    @JdbcTypeCode(Types.BLOB)
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private Blob defectImageBlob;

    public Defect(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public Defect(Vehicle vehicle, String defectName) {
        this.vehicle = vehicle;
        this.defectName = defectName;
    }

    public Defect(String defectName, ArrayList<Location> locationList) {
        this.defectName = defectName;
        this.locationList = locationList;
    }
}

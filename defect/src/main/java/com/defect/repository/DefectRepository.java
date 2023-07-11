package com.defect.repository;

import com.defect.entities.Defect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {

    Page<Defect> findAll(Pageable pageable);

    @Query("SELECT d FROM Defect d WHERE d.vehicle.id = :vehicleId") // LEFT JOIN
    Page<Defect> findDefectsByVehicleId(@Param("vehicleId") Long vehicleId, Pageable pageable);
}

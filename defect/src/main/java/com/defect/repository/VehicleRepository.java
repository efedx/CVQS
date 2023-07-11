package com.defect.repository;

import com.defect.entities.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findById(Long id);
    Page<Vehicle> findAll(Pageable pageable);

    @Query("SELECT v FROM Vehicle v JOIN v.defectList d WHERE d.defectName = :defectName") // LEFT JOIN
    Page<Vehicle> findByDefectName(@Param("defectName") String defectName, Pageable pageable);

//    @Query("SELECT v FROM Vehicle v JOIN v.defectList d WHERE d.defectName = :defectName") // LEFT JOIN
//    List<Vehicle> findByDefectName(@Param("defectName") String defectName, Pageable pageable);

}
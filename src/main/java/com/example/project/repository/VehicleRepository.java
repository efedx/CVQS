package com.example.project.repository;

import com.example.project.model.Employee;
import com.example.project.model.Vehicle;
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

//    @Query("SELECT v FROM vehicles c LEFT JOIN FETCH v.defects.defectName d")
//    List<Vehicle> findAllWithDefectNames();

    //List<Vehicle> findAll();
    Optional<Vehicle> findById(Long id);
    Page<Vehicle> findAll(Pageable pageable);

    // SELECT * FROM Vehicle-class ---
    @Query("SELECT v FROM Vehicle v LEFT JOIN v.defectList d WHERE d.defectName = :defectName")
    Page<Vehicle> findByDefectName(@Param("defectName") String defectName, Pageable pageable);



    // SELECT * FROM Vehicle-class v --- v.defectList
//    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.defectList d WHERE d.defectName = ?1")
//    Page<Vehicle> findByDefectName( String defectName, Pageable pageable);
//
////    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.defectList d WHERE d.defectName = :defectName")
////    Page<Vehicle> findByDefectName(@Param(value = "defectName") String defectName, Pageable pageable);
}
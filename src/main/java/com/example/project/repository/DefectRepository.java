package com.example.project.repository;

import com.example.project.model.Defect;
import com.example.project.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {

    Page<Defect> findAll(Pageable pageable);

}

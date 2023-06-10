package com.example.repository;

import com.example.model.Defect;
import com.example.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {

    Page<Defect> findAll(Pageable pageable);

}

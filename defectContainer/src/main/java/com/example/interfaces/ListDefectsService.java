package com.example.interfaces;

import com.example.model.Defect;
import com.example.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ListDefectsService {
    Page<Defect> getDefectsByVehicle(String authorizationHeader, Long vehicleId, int pageNumber, String sortField, String sortDirection);

    @Transactional
    Page<Vehicle> getDefectsByVehiclePage(String authorizationHeader, int pageNumber, String sortField, String sortDirection);

    @Transactional
    Page<Vehicle> getDefectsByVehiclePage(String authorizationHeader, int pageNumber, String sortField, String sortDirection, String defectName);
}

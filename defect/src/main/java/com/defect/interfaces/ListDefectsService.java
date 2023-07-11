package com.defect.interfaces;

import com.defect.entities.Defect;
import com.defect.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ListDefectsService {
    Page<Defect> getDefectsByVehicleId(Long vehicleId, int pageNumber, String sortField, String sortDirection);

    @Transactional
    Page<Vehicle> getAllDefects(int pageNumber, String sortField, String sortDirection);

    @Transactional
    Page<Vehicle> getAllDefects(int pageNumber, String sortField, String sortDirection, String defectName);
}

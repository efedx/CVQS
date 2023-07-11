package com.defect.services;

import com.defect.exceptions.NoVehicleWithIdException;
import com.defect.repository.DefectRepository;
import com.defect.repository.VehicleRepository;
import com.defect.entities.Defect;
import com.defect.entities.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListDefectsService implements com.defect.interfaces.ListDefectsService {

    private final VehicleRepository vehicleRepository;
    private final DefectRepository defectRepository;

    /**
     * Retrieves a page of defects associated with a specific vehicle.
     *
     * @param vehicleId           The ID of the vehicle for which defects are requested.
     * @param pageNumber          The page number to retrieve (1-based index).
     * @param sortField           The field to use for sorting the defects.
     * @param sortDirection       The direction of sorting ("asc" for ascending, "desc" for descending).
     * @return A page of defects for the specified vehicle.
     */
    @Override
    public Page<Defect> getDefectsByVehicleId(Long vehicleId, int pageNumber, String sortField, String sortDirection) {

        if(!vehicleRepository.existsById(vehicleId)) throw  new NoVehicleWithIdException("Vehicle with id " + vehicleId + " does not exist");

        int itemPerPage = 4;

        Pageable pageable = PageRequest.of(pageNumber - 1, itemPerPage, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        Page<Defect> defectPage = defectRepository.findDefectsByVehicleId(vehicleId, pageable);

        return defectPage;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a page of vehicles with their associated defects.
     *
     * @param pageNumber          The page number to retrieve (1-based index).
     * @param sortField           The field to use for sorting the vehicles.
     * @param sortDirection       The direction of sorting ("asc" for ascending, "desc" for descending).
     * @return A page of vehicles with their associated defects.
     */
    @Override
    @Transactional
    public Page<Vehicle> getAllDefects(int pageNumber, String sortField, String sortDirection) {

        int itemPerPage = 4;
        Pageable pageable = PageRequest.of(pageNumber - 1, itemPerPage, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);

        return vehiclesPage;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a page of vehicles with their associated defects filtered by defect name.
     *
     * @param pageNumber          The page number to retrieve (1-based index).
     * @param sortField           The field to use for sorting the vehicles.
     * @param sortDirection       The direction of sorting ("asc" for ascending, "desc" for descending).
     * @param defectName          The name of the defect to filter by.
     * @return A page of vehicles with their associated defects filtered by defect name.
     */
    @Override
    @Transactional
    public Page<Vehicle> getAllDefects(int pageNumber, String sortField, String sortDirection, String defectName) {

        int itemPerPage = 4;
        Pageable pageable = PageRequest.of(pageNumber - 1, itemPerPage, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        Page<Vehicle> vehiclesPage = vehicleRepository.findByDefectName(defectName, pageable);

        return vehiclesPage;
    }
}
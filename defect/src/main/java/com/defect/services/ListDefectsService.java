package com.defect.services;

import com.defect.repository.DefectRepository;
import com.defect.repository.VehicleRepository;
import com.defect.model.Defect;
import com.defect.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ListDefectsService implements com.defect.interfaces.ListDefectsService {

    @Value("${url.security.listDefects}")
    private String securityListDefectsUrl;

    private final VehicleRepository vehicleRepository;
    private final DefectRepository defectRepository;
    private final RestTemplate restTemplate;

    /**
     * Retrieves a page of defects associated with a specific vehicle.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param vehicleId           The ID of the vehicle for which defects are requested.
     * @param pageNumber          The page number to retrieve (1-based index).
     * @param sortField           The field to use for sorting the defects.
     * @param sortDirection       The direction of sorting ("asc" for ascending, "desc" for descending).
     * @return A page of defects for the specified vehicle.
     */
    @Override
    public Page<Defect> getDefectsByVehicle(String authorizationHeader, Long vehicleId, int pageNumber, String sortField, String sortDirection) {

        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by("id").descending());

        return defectRepository.findAll(pageable);
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a page of vehicles with their associated defects.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param pageNumber          The page number to retrieve (1-based index).
     * @param sortField           The field to use for sorting the vehicles.
     * @param sortDirection       The direction of sorting ("asc" for ascending, "desc" for descending).
     * @return A page of vehicles with their associated defects.
     */
    @Override
    @Transactional
    public Page<Vehicle> getDefectsByVehiclePage(String authorizationHeader, int pageNumber, String sortField, String sortDirection) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        int i = 5;
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);

        return vehiclesPage;
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a page of vehicles with their associated defects filtered by defect name.
     *
     * @param authorizationHeader The authorization header containing the authentication token.
     * @param pageNumber          The page number to retrieve (1-based index).
     * @param sortField           The field to use for sorting the vehicles.
     * @param sortDirection       The direction of sorting ("asc" for ascending, "desc" for descending).
     * @param defectName          The name of the defect to filter by.
     * @return A page of vehicles with their associated defects filtered by defect name.
     */
    @Override
    @Transactional
    public Page<Vehicle> getDefectsByVehiclePage(String authorizationHeader, int pageNumber, String sortField, String sortDirection, String defectName) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by("sortField").descending());

        Page<Vehicle> vehiclesPage = vehicleRepository.findByDefectName(defectName, pageable);

        return vehiclesPage;
    }
}
package com.defect.services;

import com.defect.repository.VehicleRepository;
import com.defect.dto.RegisterDefectDto;
import com.defect.model.Defect;
import com.defect.model.Location;
import com.defect.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterDefectsService implements com.defect.interfaces.RegisterDefectsService {

    private final VehicleRepository vehicleRepository;

    //-----------------------------------------------------------------------------------------------

    /**
     * Registers defects for multiple vehicles and saves them to the database.
     *
     * @param authorizationHeader     The authorization header containing the authentication token.
     * @param registerDefectDtoList   The list of RegisterDefectDto objects containing the defect details for each vehicle.
     * @param defectImageBytes        The byte array representing the defect image.
     * @return The list of vehicles with the registered defects.
     * @throws Exception               If an error occurs during the registration or processing of the defects.
     */
    @Override
    public List<Vehicle> registerDefects(String authorizationHeader, List<RegisterDefectDto> registerDefectDtoList,
                                         byte[] defectImageBytes) throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

//        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityDefectsUrl, HttpMethod.POST, requestEntity, Object.class);

        List<Vehicle> vehicleList = new ArrayList<>();

        for(RegisterDefectDto registerDefectDto : registerDefectDtoList) {
            Vehicle vehicle = new Vehicle();

            List<Defect> defectList = defectDtoToDefectList(vehicle, registerDefectDto, defectImageBytes);

            vehicle.setVehicleNo(registerDefectDto.getVehicleNo());
            vehicle.setDefectList(defectList);

            vehicleList.add(vehicleRepository.save(vehicle));
        }

        return vehicleList;
    }

    //-----------------------------------------------------------------------------------------------

    // returns a List<Defect> populated with locations given in a LogDefectDto object
    private List<Defect> defectDtoToDefectList(Vehicle vehicle, RegisterDefectDto registerDefectDto, byte[] defectImageByte) throws Exception {

        List<Defect> defectsList = new ArrayList<>();
        Blob defectImageBlob = new SerialBlob(defectImageByte);

        // defectDto has a defect name and locations specified with that defect. Such as ["A", (100, 200), (500, 600)]
        for(RegisterDefectDto.DefectDto defectDto: registerDefectDto.getDefectList()) {

            List<Location> locationList = new ArrayList<>();

            Defect defect = new Defect(defectDto.getDefectName(),locationList, vehicle, defectImageBlob);

            // for the specified defect gives the locations by one by
            for(RegisterDefectDto.LocationDto locationDto: defectDto.getLocationList()) {

                Location location = new Location(defect, locationDto.getLocation());
                locationList.add(location);
            }

            defect.setDefectName(defectDto.getDefectName());
            defect.setLocationList(locationList);
            defectsList.add(defect);
        }
        return defectsList;
    }
}
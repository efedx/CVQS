package com.example.services;

import com.example.dto.RegisterDefectDto;
import com.example.model.Defect;
import com.example.model.Location;
import com.example.model.Vehicle;
import com.example.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterDefectsService {
    private static final String securityDefectsUrl = "http://security:8083/defects";
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    RestTemplate restTemplate;

    public List<Vehicle> registerDefects(String authorizationHeader, List<RegisterDefectDto> registerDefectDtoList,
                                  byte[] defectImageBytes) throws Exception {

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.set("Authorization", authorizationHeader);
//        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);
//
//        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityDefectsUrl, HttpMethod.POST, requestEntity, Object.class);

        //ObjectMapper objectMapper = new ObjectMapper();
        //Vehicle vehicle = objectMapper.convertValue(logDefectDto, Vehicle.class);

        List<Vehicle> vehicleList = new ArrayList<>();

        for(RegisterDefectDto registerDefectDto : registerDefectDtoList) {
            Vehicle vehicle = new Vehicle();

            List<Defect> defectList = defectDto2Defect(vehicle, registerDefectDto, defectImageBytes);

            vehicle.setVehicleNo(registerDefectDto.getVehicleNo());
            vehicle.setDefectList(defectList);

            vehicleList.add(vehicleRepository.save(vehicle));
        }

        return vehicleList;
    }

    // returns a List<Defect> populated with locations given in a LogDefectDto object
    private List<Defect> defectDto2Defect(Vehicle vehicle, RegisterDefectDto registerDefectDto, byte[] defectImageByte) throws Exception {

        List<Defect> defectsList = new ArrayList<>();
        Blob defectImageBlob = new SerialBlob(defectImageByte);

        // Byte[] defectImageBytes = defectImage.getBytes();

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
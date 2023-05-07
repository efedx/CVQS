package com.example.project.services;

import com.example.project.dto.registerDefectDto;
import com.example.project.model.Defect;
import com.example.project.model.Location;
import com.example.project.model.Vehicle;
import com.example.project.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterDefectsService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public String logDefects(List<registerDefectDto> registerDefectDtoList, byte[] defectImageBytes) throws Exception {

        //ObjectMapper objectMapper = new ObjectMapper();
        //Vehicle vehicle = objectMapper.convertValue(logDefectDto, Vehicle.class);

        for(registerDefectDto registerDefectDto : registerDefectDtoList) {
            Vehicle vehicle = new Vehicle();

            List<Defect> defectList = defectDto2Defect(vehicle, registerDefectDto, defectImageBytes);

            vehicle.setVehicleNo(registerDefectDto.getVehicleNo());
            vehicle.setDefectList(defectList);

            vehicleRepository.save(vehicle);
        }

        return "Defect logged";
    }

    // returns a List<Defect> populated with locations given in a LogDefectDto object
    private List<Defect> defectDto2Defect(Vehicle vehicle, registerDefectDto registerDefectDto, byte[] defectImageByte) throws Exception {

        List<Defect> defectsList = new ArrayList<>();
        Blob defectImageBlob = new SerialBlob(defectImageByte);

        // Byte[] defectImageBytes = defectImage.getBytes();

        // defectDto has a defect name and locations specified with that defect. Such as ["A", (100, 200), (500, 600)]
        for(registerDefectDto.DefectDto defectDto: registerDefectDto.getDefectList()) {

            List<Location> locationList = new ArrayList<>();

            Defect defect = new Defect(defectDto.getDefectName(),locationList, vehicle, defectImageBlob);

            // for the specified defect gives the locations by one by
            for(registerDefectDto.LocationDto locationDto: defectDto.getLocationList()) {

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
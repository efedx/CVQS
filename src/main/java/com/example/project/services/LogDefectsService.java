package com.example.project.services;

import com.example.project.dto.LogDefectsDto;
import com.example.project.model.Defect;
import com.example.project.model.Location;
import com.example.project.repository.LocationRepository;
import com.example.project.model.Vehicle;
import com.example.project.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class LogDefectsService {

    @Autowired
    private VehicleRepository vehicleRepository;
    private final LocationRepository locationRepository;

    public String logDefects(LogDefectsDto logDefectsDto) {

        ObjectMapper objectMapper = new ObjectMapper();


//        Vehicle vehicle = new Vehicle(logDefectsDto.getVehicleNo());
//        Defect defects = new Defect(vehicle);
//
//        vehicle.setDefectList(getDefectList(vehicle, defects, logDefectsDto));

        vehicleRepository.save(vehicle);

        return "thank god";
    }

//    ArrayList<Defect> getDefectList(Vehicle vehicle, Defect defects, LogDefectsDto logDefectsDto) {
//
//        String[] defectListDto = logDefectsDto.getDefects();
//        int[][][] locationListDto = logDefectsDto.getLocations();
//
//        // create a list of defects to return
//        ArrayList<Defect> defectList = new ArrayList<>();
//
//        // to get the values of defect names
//        for(int i = 0; i < defectListDto.length; i++) {
//
//            String defectName = defectListDto[i];
//
//            // to get the locations for a particular defect with same index
//            for(int j = 0; j < locationListDto[i].length; j++) {
//
//                ArrayList<Location> locationList = new ArrayList<>();
//                Location location = new Location(defects, locationListDto[i][j]);
//
//                locationList.add(location);
//
//                Defect defect = new Defect(defectName, locationList, vehicle);
//                defectList.add(defect);
//            }
//        }
//        return defectList;
//    }

}

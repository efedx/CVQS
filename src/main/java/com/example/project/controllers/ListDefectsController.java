package com.example.project.controllers;

import com.example.project.model.Defect;
import com.example.project.model.Vehicle;
import com.example.project.services.DefectImageService;
import com.example.project.services.ListDefectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("listDefects")
@RequiredArgsConstructor
public class ListDefectsController {

    @Autowired
    ListDefectsService listDefectsService;
    @Autowired
    DefectImageService defectImageService;

//    @GetMapping("getDefectImage/{defectId}")
//    public DefectImageResponseDto getdDefectImage(@PathVariable Long defectId) {
//        return listDefectsService.getDefectImage(defectId);
//    }

    @GetMapping("getDefectImage/{defectId}")
    public ResponseEntity<byte[]> getDefectImage(@PathVariable Long defectId) throws Exception {
        byte[] combinedImageByte = defectImageService.getDefectImage(defectId);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(combinedImageByte);
    }

    @GetMapping("getDefectsByVehicle/{vehicleId}/page/{pageNumber}")
    public ResponseEntity<Page<Defect>> getVehiclesPage(@PathVariable Long vehicleId, @PathVariable int pageNumber,
                                                        @RequestParam String sortDirection, @RequestParam String sortField) {
        return ResponseEntity.ok().body(listDefectsService.getDefectsByVehicle(vehicleId, pageNumber, sortField, sortDirection));
    }

    @GetMapping("getVehicles/page/{pageNumber}")
    public ResponseEntity<Page<Vehicle>> getVehiclesPage(@PathVariable int pageNumber, @RequestParam String sortDirection,
                                                         @RequestParam String sortField, @RequestParam(required = false) String defectName) {
        if(defectName != null) {
            return ResponseEntity.ok().body(listDefectsService.getVehicles(pageNumber, sortField, sortDirection, defectName));
        }
        else {
            return ResponseEntity.ok().body(listDefectsService.getVehicles(pageNumber, sortField, sortDirection));
        }
    }

//    @GetMapping("getDefects/page/{pageNumber}")
//    public ResponseEntity<Page<Defect>> getDefectspage(@PathVariable int pageNumber, @RequestParam String sortDirection) {
//        return ResponseEntity.ok().body(listDefectsService.getDefectsByVehicle(pageNumber, sortDirection));
//    }

}

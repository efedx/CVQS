package com.example.controllers;

import com.example.dto.RegisterDefectDto;
import com.example.model.Defect;
import com.example.model.Vehicle;
import com.example.services.DefectImageService;
import com.example.services.ListDefectsService;
import com.example.services.RegisterDefectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
//@RequestMapping("/defects")
@RequiredArgsConstructor
public class DefectsController {

    @Autowired
    ListDefectsService listDefectsService;
    @Autowired
    DefectImageService defectImageService;
    @Autowired
    RegisterDefectsService registerDefectsService;

//    @GetMapping("getDefectImage/{defectId}")
//    public DefectImageResponseDto getdDefectImage(@PathVariable Long defectId) {
//        return listDefectsService.getDefectImage(defectId);
//    }

    @GetMapping("/defects/getDefectImage/{defectId}")
    public ResponseEntity<byte[]> getDefectImage(@RequestHeader("Authorization") String authorizationHeader,
                                                 @PathVariable Long defectId) throws Exception {

        byte[] combinedImageByte = defectImageService.getDefectImage(authorizationHeader, defectId);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(combinedImageByte);
    }

    @GetMapping("/defects/getDefectsByVehicle/{vehicleId}/page/{pageNumber}")
    public ResponseEntity<Page<Defect>> getVehiclesPage(@RequestHeader("Authorization") String authorizationHeader,
                                                        @PathVariable Long vehicleId,
                                                        @PathVariable int pageNumber,
                                                        @RequestParam String sortDirection,
                                                        @RequestParam String sortField) {
        return ResponseEntity.ok().body(listDefectsService.getDefectsByVehicle(authorizationHeader, vehicleId, pageNumber, sortField, sortDirection));
    }

    @GetMapping("/defects/getDefectsByVehicle/page/{pageNumber}")
    public ResponseEntity<Page<Vehicle>> getDefectsByVehiclePage(@RequestHeader("Authorization") String authorizationHeader,
                                                                 @PathVariable int pageNumber,
                                                                 @RequestParam String sortDirection,
                                                                 @RequestParam String sortField,
                                                                 @RequestParam(required = false) String defectName) {
        if(defectName != null) {
            return ResponseEntity.ok().body(listDefectsService.getDefectsByVehiclePage(authorizationHeader, pageNumber, sortField, sortDirection, defectName));
        }
        else {
            return ResponseEntity.ok().body(listDefectsService.getDefectsByVehiclePage(authorizationHeader, pageNumber, sortField, sortDirection));
        }
    }

    @PostMapping("/registerDefects")
    public String registerDefects(@RequestPart("registerDefectDto") List<RegisterDefectDto> registerDefectDtoList,
                                  @RequestPart("defectImage") MultipartFile defectImage,
                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        byte[] defectImageByte;

        try {
            defectImageByte = defectImage.getBytes();
        } catch (IOException e) {
            // handle the exception, e.g. log an error message or return an error response
            return "An error occurred while creating defect image";
        }
        registerDefectsService.registerDefects(authorizationHeader, registerDefectDtoList, defectImageByte);
        return "Defects saved";
    }

}

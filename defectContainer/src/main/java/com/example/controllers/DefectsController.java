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
    public ResponseEntity<byte[]> getDefectImage(@PathVariable Long defectId,
                                                 @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        byte[] combinedImageByte = defectImageService.getDefectImage(defectId);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(combinedImageByte);
    }

    @GetMapping("/defects/getDefectsByVehicle/{vehicleId}/page/{pageNumber}")
    public ResponseEntity<Page<Defect>> getVehiclesPage(@PathVariable Long vehicleId,
                                                        @PathVariable int pageNumber,
                                                        @RequestParam String sortDirection,
                                                        @RequestParam String sortField,
                                                        @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok().body(listDefectsService.getDefectsByVehicle(vehicleId, pageNumber, sortField, sortDirection, authorizationHeader));
    }

    @GetMapping("/defects/getDefectsByVehicle/page/{pageNumber}")
    public ResponseEntity<Page<Vehicle>> getDefectsByVehiclePage(@PathVariable int pageNumber,
                                                                 @RequestParam String sortDirection,
                                                                 @RequestParam String sortField,
                                                                 @RequestParam(required = false) String defectName,
                                                                 @RequestHeader("Authorization") String authorizationHeader) {
        if(defectName != null) {
            return ResponseEntity.ok().body(listDefectsService.getDefectsByVehiclePage(pageNumber, sortField, sortDirection, defectName, authorizationHeader));
        }
        else {
            return ResponseEntity.ok().body(listDefectsService.getDefectsByVehiclePage(pageNumber, sortField, sortDirection, authorizationHeader));
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
            return "error";
        }

        return registerDefectsService.registerDefects(registerDefectDtoList, defectImageByte, authorizationHeader);
    }

}

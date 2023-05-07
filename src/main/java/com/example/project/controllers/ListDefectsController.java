package com.example.project.controllers;

import com.example.project.services.ListDefectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class ListDefectsController {

    @Autowired
    ListDefectsService listDefectsService;

//    @GetMapping("getDefectImage/{defectId}")
//    public DefectImageResponseDto getdDefectImage(@PathVariable Long defectId) {
//        return listDefectsService.getDefectImage(defectId);
//    }

    @GetMapping("getDefectImage/{defectId}")
    public ResponseEntity<byte[]> getdDefectImage(@PathVariable Long defectId) throws Exception {
        byte[] combinedImageByte = listDefectsService.getDefectImage(defectId);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(combinedImageByte);
    }


}

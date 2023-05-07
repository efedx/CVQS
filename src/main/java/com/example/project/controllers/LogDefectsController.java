package com.example.project.controllers;

import com.example.project.dto.LogDefectDto;
import com.example.project.services.LogDefectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController(value = "defectLog")
@RequiredArgsConstructor
public class LogDefectsController {
    @Autowired
    LogDefectsService logDefectsService;

    @PostMapping("logDefects")
    public String logDefects(@RequestPart("logDefectDto") LogDefectDto logDefectDto, @RequestPart("defectImage") MultipartFile defectImage) throws Exception {


        byte[] defectImageByte;

        try {
            defectImageByte = defectImage.getBytes();
        } catch (IOException e) {
            // handle the exception, e.g. log an error message or return an error response
            return "error";
        }

        return logDefectsService.logDefects(logDefectDto, defectImageByte);
    }
}

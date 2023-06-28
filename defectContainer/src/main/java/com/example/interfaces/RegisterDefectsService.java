package com.example.interfaces;

import com.example.dto.RegisterDefectDto;
import com.example.model.Vehicle;

import java.util.List;

public interface RegisterDefectsService {
    List<Vehicle> registerDefects(String authorizationHeader,
                                  List<RegisterDefectDto> registerDefectDtoList,
                                  byte[] defectImageBytes) throws Exception;
}

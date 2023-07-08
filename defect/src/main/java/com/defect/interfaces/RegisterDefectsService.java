package com.defect.interfaces;

import com.defect.dto.RegisterDefectDto;
import com.defect.model.Vehicle;

import java.util.List;

public interface RegisterDefectsService {
    List<Vehicle> registerDefects(String authorizationHeader,
                                  List<RegisterDefectDto> registerDefectDtoList,
                                  byte[] defectImageBytes) throws Exception;
}

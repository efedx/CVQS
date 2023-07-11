package com.defect.interfaces;

import com.defect.dto.RegisterDefectDto;
import com.defect.entities.Vehicle;

import java.util.List;

public interface RegisterDefectsService {
    List<Vehicle> registerDefects(List<RegisterDefectDto> registerDefectDtoList,
                                  byte[] defectImageBytes) throws Exception;
}

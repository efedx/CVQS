package com.defect.interfaces;

import org.springframework.transaction.annotation.Transactional;

public interface DefectImageService {
    @Transactional
    byte[] getDefectImage(String authorizationHeader, Long defectId) throws Exception;
}

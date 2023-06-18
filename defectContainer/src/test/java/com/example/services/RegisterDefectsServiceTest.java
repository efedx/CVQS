package com.example.services;

import com.example.dto.RegisterDefectDto;
import com.example.model.Defect;
import com.example.model.Location;
import com.example.model.Vehicle;
import com.example.repository.VehicleRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterDefectsServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    RestTemplate restTemplate;
    @Mock
    private RegisterDefectsService underTestRegisterDefectsService;

    @BeforeEach
    void setUp() {
        //underTestRegisterDefectsService = new RegisterDefectsService(vehicleRepository, restTemplate);
    }

    @Test
    void registerDefects() {

        RegisterDefectDto.LocationDto location1 = new RegisterDefectDto.LocationDto();
        ArrayList<Integer> locationList1 = new ArrayList<>();
        locationList1.add(10);
        locationList1.add(20);
        location1.setLocation(locationList1);

        // Create DefectDto objects
        RegisterDefectDto.DefectDto defect1 = new RegisterDefectDto.DefectDto();
        defect1.setDefectName("Defect 1");

        RegisterDefectDto.DefectDto defect2 = new RegisterDefectDto.DefectDto();
        defect2.setDefectName("Defect 2");

        ArrayList<RegisterDefectDto.LocationDto> locationList4 = new ArrayList<>();
        locationList4.add(location1);
        defect2.setLocationList(locationList4);

        // Create RegisterDefectDto object and set values
        RegisterDefectDto registerDefectDto = new RegisterDefectDto();

        registerDefectDto.setVehicleNo(12345L);
        ArrayList<RegisterDefectDto.DefectDto> defectList = new ArrayList<>();
        defectList.add(defect1);
        defectList.add(defect2);

        registerDefectDto.setDefectList(defectList);



        // given

        // when

        // then

    }
}
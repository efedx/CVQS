package com.defect.services;

import com.defect.dto.RegisterDefectDto;
import com.defect.entities.Defect;
import com.defect.entities.Vehicle;
import com.defect.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterDefectsServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @InjectMocks
    private RegisterDefectsService underTestRegisterDefectsService;


    @Test
    void registerDefects() throws Exception {

        // given
        RegisterDefectDto.LocationDto locationDto = new RegisterDefectDto.LocationDto();
        locationDto.setLocation(List.of(100, 200));
        List<RegisterDefectDto.LocationDto> locationDtoList = List.of(locationDto);

        RegisterDefectDto.DefectDto defectDto = new RegisterDefectDto.DefectDto();
        defectDto.setDefectName("defect name");
        defectDto.setLocationList(locationDtoList);
        List<RegisterDefectDto.DefectDto> defectDtoList = List.of(defectDto);

        RegisterDefectDto registerDefectDto = new RegisterDefectDto();
        registerDefectDto.setVehicleNo(1L);
        registerDefectDto.setDefectList(defectDtoList);
        List<RegisterDefectDto> registerDefectDtoList = List.of(registerDefectDto);

        byte[] imageBytes = {1, 2, 3, 4};

        Vehicle vehicle = new Vehicle();
        List<Defect> defectList = underTestRegisterDefectsService.defectDtoToDefectList(vehicle, registerDefectDto, imageBytes);
        vehicle.setVehicleNo(1L);
        vehicle.setDefectList(defectList);

        // when
        underTestRegisterDefectsService.registerDefects(registerDefectDtoList, imageBytes);

        // then
        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleRepository).save(vehicleArgumentCaptor.capture());
        Vehicle vehicleCaptured = vehicleArgumentCaptor.getValue();

        assertThat(vehicleCaptured.getVehicleNo()).isEqualTo(vehicle.getVehicleNo());
        assertThat(vehicleCaptured.getDefectList().size()).isEqualTo(vehicle.getDefectList().size());
        assertThat(vehicleCaptured.getDefectList().get(0).getDefectName()).isEqualTo(vehicle.getDefectList().get(0).getDefectName());
        assertThat(vehicleCaptured.getDefectList().get(0).getLocationList().size()).isEqualTo(vehicle.getDefectList().get(0).getLocationList().size());

    }

}
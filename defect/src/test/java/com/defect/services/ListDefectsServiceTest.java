package com.defect.services;

import com.defect.entities.Defect;
import com.defect.entities.Vehicle;
import com.defect.exceptions.NoVehicleWithIdException;
import com.defect.repository.DefectRepository;
import com.defect.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ListDefectsServiceTest {

    @Mock
    DefectRepository defectRepository;
    @Mock
    VehicleRepository vehicleRepository;
    @InjectMocks
    ListDefectsService underTestListDefectsService;

    @Test
    void getDefectsByVehicleId() {

        // given
        Long vehicleId = 1L;
        int itemPerPage = 4;
        int pageNumber = 1;

        String sortDirectionAsc = "asc";
        String sortDirectionDesc = "desc";
        String sortField = "defectName";

        Pageable pageableAsc = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by(sortField).ascending());
        Pageable pageableDesc = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by(sortField).descending());

        // when
        when(vehicleRepository.existsById(anyLong())).thenReturn(true);
        underTestListDefectsService.getDefectsByVehicleId(vehicleId, pageNumber, sortField, sortDirectionAsc);
        underTestListDefectsService.getDefectsByVehicleId(vehicleId, pageNumber, sortField, sortDirectionDesc);

        // then
        verify(defectRepository).findDefectsByVehicleId(vehicleId, pageableAsc);
        verify(defectRepository).findDefectsByVehicleId(vehicleId, pageableDesc);
    }

    @Test
    void shouldThrowNoVehicleWithIdException() {
        // given
        Long vehicleId = 1L;
        int itemPerPage = 5;
        int pageNumber = 1;
        String sortField = "defectName";
        String sortDirectionAsc = "asc";
        // when
        when(vehicleRepository.existsById(anyLong())).thenReturn(false);
        // then
        assertThatThrownBy(() -> underTestListDefectsService.getDefectsByVehicleId(vehicleId, pageNumber, sortField, sortDirectionAsc))
                .isInstanceOf(NoVehicleWithIdException.class)
                .hasMessage("Vehicle with id " + vehicleId + " does not exist");
    }

    @Test
    void getAllDefectsWithoutDefectName() {

        // given
        int itemPerPage = 4;
        int pageNumber = 1;

        String sortDirectionAsc = "asc";
        String sortDirectionDesc = "desc";
        String sortField = "defectName";

        Pageable pageableAsc = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by(sortField).ascending());
        Pageable pageableDesc = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by(sortField).descending());

        Page<Vehicle> mockPage = Mockito.mock(Page.class);

        // when
        when(vehicleRepository.findAll(pageableAsc)).thenReturn(mockPage);
        when(vehicleRepository.findAll(pageableDesc)).thenReturn(mockPage);

        underTestListDefectsService.getAllDefects(pageNumber, sortField, sortDirectionAsc);
        underTestListDefectsService.getAllDefects(pageNumber, sortField, sortDirectionDesc);

        // then
        verify(vehicleRepository).findAll(pageableAsc);
        verify(vehicleRepository).findAll(pageableDesc);
    }

    @Test
    void getAllDefectsWithDefectName() {
        // given
        String defectName = "defect";

        int itemPerPage = 4;
        int pageNumber = 1;

        String sortDirectionAsc = "asc";
        String sortDirectionDesc = "desc";
        String sortField = "defectName";

        Pageable pageableAsc = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by(sortField).ascending());
        Pageable pageableDesc = PageRequest.of(pageNumber - 1, itemPerPage, Sort.by(sortField).descending());

        Page<Vehicle> mockPage = Mockito.mock(Page.class);

        // when
        when(vehicleRepository.findByDefectName(defectName, pageableAsc)).thenReturn(mockPage);
        when(vehicleRepository.findByDefectName(defectName, pageableDesc)).thenReturn(mockPage);
        underTestListDefectsService.getAllDefects(pageNumber, sortField, sortDirectionAsc, defectName);
        underTestListDefectsService.getAllDefects(pageNumber, sortField, sortDirectionDesc, defectName);

        // then
        verify(vehicleRepository).findByDefectName(defectName, pageableAsc);
        verify(vehicleRepository).findByDefectName(defectName, pageableDesc);

    }
}
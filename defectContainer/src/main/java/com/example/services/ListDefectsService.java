package com.example.services;

import com.example.model.Defect;
import com.example.model.Vehicle;
import com.example.repository.DefectRepository;
import com.example.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ListDefectsService implements com.example.interfaces.ListDefectsService {

    @Value("${url.security.listDefects}")
    private String securityListDefectsUrl;

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    DefectRepository defectRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Page<Defect> getDefectsByVehicle(String authorizationHeader, Long vehicleId, int pageNumber, String sortField, String sortDirection) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityListDefectsUrl, HttpMethod.POST, requestEntity, Object.class);

        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by("id").descending());

        return defectRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Vehicle> getDefectsByVehiclePage(String authorizationHeader, int pageNumber, String sortField, String sortDirection) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityListDefectsUrl, HttpMethod.POST, requestEntity, Object.class);

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

//        List<Vehicle> vehiclesPage = vehicleRepository.findAllWithDefectNames();
        int i = 5;
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);

        return vehiclesPage;
    }

    @Override
    @Transactional
    public Page<Vehicle> getDefectsByVehiclePage(String authorizationHeader, int pageNumber, String sortField, String sortDirection, String defectName) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityListDefectsUrl, HttpMethod.POST, requestEntity, Object.class);

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by("sortField").descending());

        Page<Vehicle> vehiclesPage = vehicleRepository.findByDefectName(defectName, pageable);

        return vehiclesPage;
    }
}
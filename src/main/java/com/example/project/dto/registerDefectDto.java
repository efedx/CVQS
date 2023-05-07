package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class registerDefectDto {
    // private String[][][] defects;

    private Long vehicleNo;
    //private Defects[] defects;
    private ArrayList<DefectDto> defectList;

    @Data
    public static class DefectDto {
        private String defectName;
        //private Byte[] defectImage;
        // private ArrayList<ArrayList<Integer>> locationList;
        private ArrayList<LocationDto> locationList;
    }

    @Data
    public static class LocationDto {
        private ArrayList<Integer> location;
    }

//    private ArrayList<Defect> defectList = new ArrayList<>();
//    private ArrayList<ArrayList<Integer>> locationList = new ArrayList<>(); // Location inside
//    private Integer[] location;
}

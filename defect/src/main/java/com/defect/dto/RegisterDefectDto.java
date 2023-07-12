package com.defect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDefectDto {
    // private String[][][] defects;

    private Long vehicleNo;
    //private Defects[] defects;
    private List<DefectDto> defectList;

    @Data
    public static class DefectDto {
        private String defectName;
        //private Byte[] defectImage;
        // private ArrayList<ArrayList<Integer>> locationList;
        private List<LocationDto> locationList;
    }

    @Data
    public static class LocationDto {
        private List<Integer> location;
    }

//    private ArrayList<Defect> defectList = new ArrayList<>();
//    private ArrayList<ArrayList<Integer>> locationList = new ArrayList<>(); // Location inside
//    private Integer[] location;
}

//package com.example.project.services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Test {
//    import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.ArrayList;
//import java.util.List;
//
//    public class VehicleDefectsDTOConverter {
//        public static void main(String[] args) {
//            String json = "{ \"vehicleNo\": 1, \"defects\": [ { \"defectNames\": \"A\", \"locations\": [[1,2], [1,2], [1,2]] }, { \"defectNames\": \"A\", \"locations\": [[1,2], [1,2], [1,2]] }, { \"defectNames\": \"A\", \"locations\": [[1,2], [1,2], [1,2]] } ] }";
//
//            ObjectMapper mapper = new ObjectMapper();
//            VehicleDefectsDTO vehicleDefectsDTO = new VehicleDefectsDTO();
//
//            try {
//                JsonNode jsonNode = mapper.readTree(json);
//                vehicleDefectsDTO.setVehicleNo(jsonNode.get("vehicleNo").asInt());
//
//                List<VehicleDefectsDTO.DefectDTO> defects = new ArrayList<>();
//                JsonNode defectsNode = jsonNode.get("defects");
//
//                for (JsonNode defectNode : defectsNode) {
//                    VehicleDefectsDTO.DefectDTO defectDTO = new VehicleDefectsDTO.DefectDTO();
//                    defectDTO.setDefectNames(defectNode.get("defectNames").asText());
//
//                    List<List<Integer>> locations = new ArrayList<>();
//                    JsonNode locationsNode = defectNode.get("locations");
//
//                    for (JsonNode locationNode : locationsNode) {
//                        List<Integer> location = new ArrayList<>();
//                        JsonNode xNode = locationNode.get(0);
//                        JsonNode yNode = locationNode.get(1);
//                        location.add(xNode.asInt());
//                        location.add(yNode.asInt());
//                        locations.add(location);
//                    }
//
//
//                    defects.add(defectDTO);
//                }
//
//
//                System.out.println(vehicleDefectsDTO);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        public static class VehicleDefectsDTO {
//            private int vehicleNo;
//            private List<DefectDTO> defects;
//            }
//
//            public static class DefectDTO {
//                private String defectNames;
//                private List<List<Integer>> locations;
//                }
//
//            }
//        }
//    }
//}

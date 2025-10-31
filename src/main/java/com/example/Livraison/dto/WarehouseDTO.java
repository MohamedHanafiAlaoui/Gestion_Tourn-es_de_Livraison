package com.example.Livraison.dto;


import com.example.Livraison.model.Tour;
import com.example.Livraison.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseDTO {

    private Long id;
    private String adresse;
    private double gpsLat;
    private double gpsLong;
    private String horaireOuverture;
    private String horaireFermeture;
    private List<Long> tourIds;

    public  static WarehouseDTO toDto(Warehouse warehouse){
        return WarehouseDTO
                .builder()
                .id(warehouse.getId())
                .adresse(warehouse.getAdresse())
                .gpsLat(warehouse.getGpsLat())
                .gpsLong(warehouse.getGpsLong())
                .horaireOuverture(warehouse.getHoraireOuverture())
                .horaireFermeture(warehouse.getHoraireFermeture())
                .tourIds(  warehouse.getTours() != null
                        ? warehouse.getTours().stream()
                        .map(Tour::getId)
                        .toList()
                        : null)
                .build();
    }





}

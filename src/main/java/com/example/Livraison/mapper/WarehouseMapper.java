package com.example.Livraison.mapper;

import com.example.Livraison.dto.WarehouseDTO;
import com.example.Livraison.model.Warehouse;

public class WarehouseMapper {

    private WarehouseMapper(){}

    public static WarehouseDTO toDto(Warehouse warehouse)
    {
        if (warehouse == null) throw   new IllegalArgumentException("Warehouse is null");

        return  WarehouseDTO.toDto(warehouse);
    }

    public static  Warehouse toEntity(WarehouseDTO dto)
    {
        if (dto == null) throw   new IllegalArgumentException("WarehouseDTO is null");
        return    Warehouse.builder()
                .id(dto.getId())
                .adresse(dto.getAdresse())
                .gpsLat(dto.getGpsLat())
                .gpsLong(dto.getGpsLong())
                .horaireFermeture(dto.getHoraireFermeture())
                .horaireOuverture(dto.getHoraireOuverture())
                .build();
    }

}

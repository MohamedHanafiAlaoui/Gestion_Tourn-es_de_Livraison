package com.example.Livraison.mapper;

import com.example.Livraison.dto.TourDTO;
import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Tour;
import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.Warehouse;

import java.util.stream.Collectors;

public class TourMapper {

    private  TourMapper() {}

    public static TourDTO toDto(Tour tour) {
        if (tour == null) throw  new IllegalArgumentException("Tour is null");
        return TourDTO.builder()
                .id(tour.getId())
                .date(tour.getDate())
                .vehiculeId(tour.getVehicule() != null ? tour.getVehicule().getId() : null)
                .warehouseId(tour.getWarehouse() != null ? tour.getWarehouse().getId() : null)
                .deliveryIds(
                        tour.getDeliveries() != null
                                ? tour.getDeliveries().stream()
                                .map(Delivery::getId)
                                .collect(Collectors.toList())
                                : null
                )
                .build();
    }
    public static Tour toEntity(TourDTO dto)
    {
        if (dto == null) throw  new IllegalArgumentException("DTO is null");
        return Tour.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .vehicule(dto.getVehiculeId() != null ? Vehicule.builder().id(dto.getVehiculeId()).build() : null)
                .warehouse(dto.getWarehouseId() != null ? Warehouse.builder().id(dto.getWarehouseId()).build() : null)
                .build();
    }
}

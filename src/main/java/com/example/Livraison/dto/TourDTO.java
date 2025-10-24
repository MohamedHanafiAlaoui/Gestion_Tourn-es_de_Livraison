package com.example.Livraison.dto;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Tour;
import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.Warehouse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TourDTO {
    private Long id;
    private Date date;
    private Long vehiculeId;
    private Long warehouseId;
    private List<Long> deliveryIds;

    public static  TourDTO fromEntity(Tour tour)
    {
        return TourDTO.builder()
                .id(tour.getId())
                .date(tour.getDate())
                .vehiculeId(tour.getVehicule() != null ? tour.getVehicule().getId() : null)
                .warehouseId(tour.getWarehouse() != null ? tour.getWarehouse().getId() : null)
                .deliveryIds(
                        tour.getDeliveries() != null
                                ? tour.getDeliveries().stream()
                                .map(Delivery::getId)
                                .toList()
                                : null
                )

                .build();
    }


}

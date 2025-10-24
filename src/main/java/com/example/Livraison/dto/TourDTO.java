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
    private Vehicule vehicule;
    private Warehouse warehouse;
    private List<Delivery> deliveries;

    public static  TourDTO tourDTO(Tour tour)
    {
        return TourDTO.builder()
                .id(tour.getId())
                .date(tour.getDate())
                .vehicule(tour.getVehicule())
                .warehouse(tour.getWarehouse())
                .deliveries(tour.getDeliveries())
                .build();
    }
}

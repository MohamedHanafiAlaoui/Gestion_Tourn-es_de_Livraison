package com.example.Livraison.mapper;

import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Tour;

public class DeliveryMapper {

    private DeliveryMapper() {}

    public static DeliveryDTO toDto(Delivery delivery)
    {
        if (delivery == null) throw  new IllegalArgumentException("Delivery is null");
        return DeliveryDTO.toDto(delivery);
    }

    public static Delivery toEntity(DeliveryDTO dto)
    {
        if (dto == null) throw  new IllegalArgumentException("Delivery is null");

        return Delivery.builder()
                .id(dto.getId())
                .adresse(dto.getAdresse())
                .gpsLat(dto.getGpsLat())
                .gpsLon(dto.getGpsLon())
                .poidsKg(dto.getPoidsKg())
                .volumeM3(dto.getVolumeM3())
                .creneauPref(dto.getCreneauPref())
                .status(dto.getStatus())
                .tour(dto.getTourId() != null ? Tour.builder().id(dto.getTourId()).build() : null)
                .build();
    }
}

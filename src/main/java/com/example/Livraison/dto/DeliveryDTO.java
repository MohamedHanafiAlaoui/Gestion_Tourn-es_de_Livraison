package com.example.Livraison.dto;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Tour;
import com.example.Livraison.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DeliveryDTO {


    private Long id;

    private String adresse;
    private double gpsLat;
    private double gpsLon;
    private double poidsKg;
    private double volumeM3;
    private String creneauPref;
    private Status status;
    private Long tourId;

    public  static DeliveryDTO  toDto  (Delivery delivery)
    {
        return DeliveryDTO
                .builder()
                .id(delivery.getId())
                .adresse(delivery.getAdresse())
                .gpsLat(delivery.getGpsLat())
                .gpsLon(delivery.getGpsLon())
                .poidsKg(delivery.getPoidsKg())
                .volumeM3(delivery.getVolumeM3())
                .creneauPref(delivery.getCreneauPref())
                .status(delivery.getStatus())
                .tourId(delivery.getTour() != null ? delivery.getTour().getId() : null)
                .build();

    }


}

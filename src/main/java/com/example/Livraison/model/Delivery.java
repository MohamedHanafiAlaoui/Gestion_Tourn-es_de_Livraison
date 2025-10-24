package com.example.Livraison.model;

import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adresse;
    private double gpsLat;
    private double gpsLon;
    private double poidsKg;
    private double volumeM3;
    private String creneauPref;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    public static Delivery fromDTO(DeliveryDTO dto) {
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

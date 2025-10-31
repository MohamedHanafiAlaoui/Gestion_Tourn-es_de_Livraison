package com.example.Livraison.model;

import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "poids_kg")

    private double poidsKg;
    @Column(name = "volume_m3")

    private double volumeM3;
    @Column(name = "creneau_pref")

    private String creneauPref;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @JsonBackReference
    private Tour tour;


}
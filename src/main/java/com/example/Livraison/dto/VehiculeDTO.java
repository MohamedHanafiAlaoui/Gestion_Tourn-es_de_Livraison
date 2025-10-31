package com.example.Livraison.dto;

import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor


public class VehiculeDTO {
    private long id;
    private TypeVehicule type;
    private  double capaciteMaxKg;
    private  double capaciteMaxM3;
    private EtatVehicule etat;
    private Date dateAjout;


    public static VehiculeDTO toDto(Vehicule vehicule) {
        if (vehicule == null) throw new IllegalArgumentException("Vehicule cannot be null");
        return VehiculeDTO.builder()
                .id(vehicule.getId())
                .type(vehicule.getType())
                .capaciteMaxKg(vehicule.getCapaciteMaxKg())
                .capaciteMaxM3(vehicule.getCapaciteMaxM3())
                .etat(vehicule.getEtat())
                .dateAjout(vehicule.getDateAjout())
                .build();
    }




}

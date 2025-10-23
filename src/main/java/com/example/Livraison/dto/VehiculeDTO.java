package com.example.Livraison.dto;

import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    public static VehiculeDTO fromEntityToDto(Vehicule vehicule)
    {
        return VehiculeDTO
                .builder()
                .id(vehicule.getId())
                .type(vehicule.getType())
                .capaciteMaxKg(vehicule.getCapaciteMaxKg())
                .capaciteMaxM3(vehicule.getCapaciteMaxM3())
                .etat(vehicule.getEtat())
                .dateAjout(vehicule.getDateAjout())
                .build();
    }



}

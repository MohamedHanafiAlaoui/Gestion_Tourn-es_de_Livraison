package com.example.Livraison.model;

import com.example.Livraison.dto.VehiculeDTO;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)

    private TypeVehicule type;
    private  double capaciteMaxKg;
    private  double capaciteMaxM3;
    @Enumerated(EnumType.STRING)
    private EtatVehicule etat;
    @Temporal(TemporalType.DATE)

    private Date dateAjout;
    @OneToMany(mappedBy = "vehicule",cascade = CascadeType.ALL)
    private List<Tour> tours;


    public Vehicule  toModels(VehiculeDTO DTO)
    {
        return  Vehicule.builder()
                .id(DTO.getId())
                .type(DTO.getType())
                .capaciteMaxKg(DTO.getCapaciteMaxKg())
                .capaciteMaxM3(DTO.getCapaciteMaxM3())
                .etat(DTO.getEtat())
                .dateAjout(DTO.getDateAjout())
                .build();
    }
}

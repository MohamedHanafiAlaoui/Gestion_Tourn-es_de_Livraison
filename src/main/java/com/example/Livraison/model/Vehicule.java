package com.example.Livraison.model;

import com.example.Livraison.dto.VehiculeDTO;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "capacite_max_kg")

    private  double capaciteMaxKg;
    @Column(name = "capacite_max_m3")

    private  double capaciteMaxM3;
    @Enumerated(EnumType.STRING)
    private EtatVehicule etat;
    @Column(name = "date_ajout")

    @Temporal(TemporalType.DATE)

    private Date dateAjout;
    @OneToMany(mappedBy = "vehicule",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tour> tours;



}
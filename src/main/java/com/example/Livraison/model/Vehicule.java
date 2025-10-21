package com.example.Livraison.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;



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
    private String type;
    private  double capaciteMaxKg;
    private  double capaciteMaxM3;
    private  String etat;
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.DATE)

    private Date dateAjout;
}

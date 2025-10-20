package com.example.Livraison.model;

import jakarta.persistence.*;
import java.util.Date;

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

    public Vehicule() {}


    public Vehicule(String type,Double capaciteMaxKg,Double capaciteMaxM3,String etat,Date dateAjout) {
        this.type = type;
        this.capaciteMaxKg = capaciteMaxKg;
        this.capaciteMaxM3 = capaciteMaxM3;
        this.etat = etat;
        this.dateAjout = dateAjout;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public double getCapaciteMaxKg() {
        return capaciteMaxKg;
    }
    public void setCapaciteMaxKg(double capaciteMaxKg) {
        this.capaciteMaxKg = capaciteMaxKg;
    }
    public double getCapaciteMaxM3() {
        return capaciteMaxM3;
    }
    public void setCapaciteMaxM3(double capaciteMaxM3) {
        this.capaciteMaxM3 = capaciteMaxM3;
    }

    public String getEtat() {
        return etat;
    }
    public void setEtat(String etat) {
        this.etat = etat;
    }
    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

}

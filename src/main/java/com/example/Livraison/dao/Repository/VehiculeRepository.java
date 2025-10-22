package com.example.Livraison.dao.Repository;

import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    List<Vehicule> findByEtat(EtatVehicule etat);

    List<Vehicule> findByType(TypeVehicule type);
}

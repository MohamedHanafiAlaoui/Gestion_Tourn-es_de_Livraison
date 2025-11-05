package com.example.Livraison.dao.Repository;

import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    List<Vehicule> findByOrderByCapaciteMaxKg();


    @Query(value = "SELECT * from vehicule  ORDER  BY capacite_max_kg DESC ", nativeQuery = true)
    List<Vehicule> findVehiculeByCapaciteMaxKg();

}

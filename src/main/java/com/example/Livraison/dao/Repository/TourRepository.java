package com.example.Livraison.dao.Repository;

import com.example.Livraison.model.Tour;
import com.example.Livraison.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
    boolean existsByVehiculeIdAndDate(long vehiculeId, java.util.Date date);
}

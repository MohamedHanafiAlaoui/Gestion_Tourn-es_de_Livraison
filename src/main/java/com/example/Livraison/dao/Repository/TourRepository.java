package com.example.Livraison.dao.Repository;

import com.example.Livraison.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Warehouse, Long> {
}

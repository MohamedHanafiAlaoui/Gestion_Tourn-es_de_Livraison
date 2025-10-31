package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;

import java.util.List;

public class TourOptimizerImpl implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Vehicule vehicule) {
        // Ensure unique deliveries by ID
        return deliveries.stream()
                .filter(d -> d.getId() != null)
                .collect(java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toMap(Delivery::getId, d -> d, (a, b) -> a),
                        m -> new java.util.ArrayList<>(m.values())
                ));
    }

    @Override
    public double getTotalDistance(List<Delivery> deliveries) {
        return 0.0;
    }
}

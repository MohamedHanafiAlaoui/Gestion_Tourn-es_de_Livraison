package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;

import java.util.List;

public interface TourOptimizer {

    List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Vehicule vehicule);
    double getTotalDistance(List<Delivery> deliveries);

}

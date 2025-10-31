package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;

import java.util.*;

public class NearestNeighborOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Vehicule vehicule) {
        if (deliveries == null || deliveries.isEmpty())
            return Collections.emptyList();

        // استعمال نسخة مباشرة بدون Map
        List<Delivery> uniqueDeliveries = new ArrayList<>(deliveries);

        if (uniqueDeliveries.size() == 1)
            return uniqueDeliveries;

        Delivery depot = uniqueDeliveries.get(0);
        List<Delivery> remaining = new ArrayList<>(uniqueDeliveries);
        remaining.remove(depot);

        List<Delivery> tour = new ArrayList<>();
        tour.add(depot);

        Delivery current = depot;
        while (!remaining.isEmpty()) {
            Delivery nearest = findNearest(current, remaining);
            tour.add(nearest);
            remaining.remove(nearest);
            current = nearest;
        }

        return tour;
    }

    @Override
    public double getTotalDistance(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.size() < 2)
            return 0.0;

        double total = 0.0;
        for (int i = 0; i < deliveries.size() - 1; i++) {
            total += distance(deliveries.get(i), deliveries.get(i + 1));
        }
        return total;
    }

    private Delivery findNearest(Delivery current, List<Delivery> remaining) {
        Delivery nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Delivery d : remaining) {
            double dist = distance(current, d);
            if (dist < minDist) {
                minDist = dist;
                nearest = d;
            }
        }
        return nearest;
    }

    private double distance(Delivery a, Delivery b) {
        double dx = a.getGpsLat() - b.getGpsLat();
        double dy = a.getGpsLon() - b.getGpsLon();
        return Math.sqrt(dx * dx + dy * dy);
    }
}

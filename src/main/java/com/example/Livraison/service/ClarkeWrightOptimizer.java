package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;

import java.util.*;

public class ClarkeWrightOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Vehicule vehicule) {

        if (deliveries == null || deliveries.isEmpty()) return Collections.emptyList();

        Delivery depot = deliveries.get(0);

        List<Delivery> clients = new ArrayList<>(deliveries.subList(1, deliveries.size()));

        Map<String, Double> distanceMap = new HashMap<>();
        for (Delivery a : deliveries) {
            for (Delivery b : deliveries) {
                if (!a.equals(b)) {
                    distanceMap.put(key(a, b), distance(a, b));
                }
            }
        }

        List<Saving> savings = new ArrayList<>();
        for (Delivery i : clients) {
            for (Delivery j : clients) {
                if (!i.equals(j)) {
                    double savingValue =
                            distanceMap.get(key(depot, i)) +
                                    distanceMap.get(key(depot, j)) -
                                    distanceMap.get(key(i, j));
                    savings.add(new Saving(i, j, savingValue));
                }
            }
        }

        savings.sort((s1, s2) -> Double.compare(s2.value, s1.value));

        List<List<Delivery>> routes = new ArrayList<>();
        for (Saving s : savings) {
            mergeRoutes(routes, s.i, s.j);
        }

        List<Delivery> bestTour = new ArrayList<>();
        bestTour.add(depot);
        if (!routes.isEmpty()) {
            bestTour.addAll(routes.get(0));
        }

        // Do not append the depot again; ensure each delivery is visited once
        return bestTour;
    }

    @Override
    public double getTotalDistance(List<Delivery> deliveries) {
        double total = 0.0;
        for (int i = 0; i < deliveries.size() - 1; i++) {
            total += distance(deliveries.get(i), deliveries.get(i + 1));
        }
        return total;
    }

    private String key(Delivery a, Delivery b) {
        return a.getId() + "-" + b.getId();
    }

    private double distance(Delivery a, Delivery b) {
        double dx = a.getGpsLat() - b.getGpsLat();
        double dy = a.getGpsLon() - b.getGpsLon();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void mergeRoutes(List<List<Delivery>> routes, Delivery i, Delivery j) {
        List<Delivery> routeI = null;
        List<Delivery> routeJ = null;

        for (List<Delivery> route : routes) {
            if (route.contains(i)) routeI = route;
            if (route.contains(j)) routeJ = route;
        }

        if (routeI == null && routeJ == null) {
            List<Delivery> newRoute = new ArrayList<>();
            newRoute.add(i);
            newRoute.add(j);
            routes.add(newRoute);
        } else if (routeI != null && routeJ == null) {
            routeI.add(j);
        } else if (routeI == null && routeJ != null) {
            routeJ.add(0, i);
        } else if (!routeI.equals(routeJ)) {
            routeI.addAll(routeJ);
            routes.remove(routeJ);
        }
    }

    private static class Saving {
        Delivery i, j;
        double value;
        public Saving(Delivery i, Delivery j, double value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }
    }
}

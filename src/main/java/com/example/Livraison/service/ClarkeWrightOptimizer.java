package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;

import java.util.*;

public class ClarkeWrightOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Vehicule vehicule) {
        if (deliveries == null || deliveries.isEmpty()) return Collections.emptyList();
        if (deliveries.size() == 1) return new ArrayList<>(deliveries);

        Delivery depot = deliveries.get(0);
        List<Delivery> clients = new ArrayList<>(deliveries.subList(1, deliveries.size()));
        if (clients.isEmpty()) {
            return new ArrayList<>(deliveries);
        }
        if (clients.size() == 1) {
            List<Delivery> single = new ArrayList<>();
            single.add(depot);
            single.add(clients.get(0));
            return single;
        }

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
                    double savingValue = distanceMap.get(key(depot, i))
                            + distanceMap.get(key(depot, j))
                            - distanceMap.get(key(i, j));
                    savings.add(new Saving(i, j, savingValue));
                }
            }
        }

        savings.sort((s1, s2) -> Double.compare(s2.value, s1.value));

        List<List<Delivery>> routes = new ArrayList<>();
        for (Saving s : savings) {
            mergeRoutes(routes, s.i, s.j);
        }

        java.util.Set<Delivery> inRoutes = new java.util.HashSet<>();
        for (List<Delivery> r : routes) {
            inRoutes.addAll(r);
        }
        for (Delivery c : clients) {
            if (!inRoutes.contains(c)) {
                List<Delivery> solo = new ArrayList<>();
                solo.add(c);
                routes.add(solo);
            }
        }

        List<Delivery> bestTour = new ArrayList<>();
        bestTour.add(depot);
        if (!routes.isEmpty()) {
            java.util.List<java.util.List<Delivery>> remaining = new java.util.ArrayList<>(routes);
            java.util.List<Delivery> chained = new java.util.ArrayList<>();

            int startIdx = -1;
            boolean reverseStart = false;
            double best = Double.MAX_VALUE;
            for (int i = 0; i < remaining.size(); i++) {
                List<Delivery> r = remaining.get(i);
                double dHead = distance(depot, r.get(0));
                double dTail = distance(depot, r.get(r.size() - 1));
                double m = Math.min(dHead, dTail);
                if (m < best) {
                    best = m;
                    startIdx = i;
                    reverseStart = dTail < dHead;
                }
            }
            List<Delivery> start = remaining.remove(startIdx);
            if (reverseStart) java.util.Collections.reverse(start);
            chained.addAll(start);

            while (!remaining.isEmpty()) {
                Delivery curEnd = chained.get(chained.size() - 1);
                int bestIdx = 0;
                boolean reverse = false;
                double bestDist = Double.MAX_VALUE;
                for (int i = 0; i < remaining.size(); i++) {
                    List<Delivery> r = remaining.get(i);
                    double dToHead = distance(curEnd, r.get(0));
                    double dToTail = distance(curEnd, r.get(r.size() - 1));
                    if (dToHead < bestDist) {
                        bestDist = dToHead; bestIdx = i; reverse = false;
                    }
                    if (dToTail < bestDist) {
                        bestDist = dToTail; bestIdx = i; reverse = true;
                    }
                }
                List<Delivery> next = remaining.remove(bestIdx);
                if (reverse) java.util.Collections.reverse(next);
                chained.addAll(next);
            }

            bestTour.addAll(chained);
        } else {
            bestTour.addAll(clients);
        }
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
        return a.getGpsLat()+","+a.getGpsLon()+"-"+b.getGpsLat()+","+b.getGpsLon();
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
            return;
        }

        if (routeI != null && routeJ == null) {
            int sizeI = routeI.size();
            if (routeI.get(sizeI - 1).equals(i)) {
                routeI.add(j);
            } else if (routeI.get(0).equals(i)) {
                routeI.add(0, j);
            }
            return;
        }

        if (routeI == null && routeJ != null) {
            int sizeJ = routeJ.size();
            if (routeJ.get(0).equals(j)) {
                routeJ.add(0, i);
            } else if (routeJ.get(sizeJ - 1).equals(j)) {
                routeJ.add(i);
            }
            return;
        }

        if (!routeI.equals(routeJ)) {
            boolean iIsTail = routeI.get(routeI.size() - 1).equals(i);
            boolean iIsHead = routeI.get(0).equals(i);
            boolean jIsHead = routeJ.get(0).equals(j);
            boolean jIsTail = routeJ.get(routeJ.size() - 1).equals(j);

            if (iIsTail && jIsHead) {
                routeI.addAll(routeJ);
                routes.remove(routeJ);
            } else if (iIsHead && jIsTail) {
                routeJ.addAll(routeI);
                routes.remove(routeI);
            }
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

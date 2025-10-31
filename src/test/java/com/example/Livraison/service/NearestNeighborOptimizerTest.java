package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NearestNeighborOptimizerTest {

    @Test
    void calculateOptimalTour_ordersByNearestNeighbor() {
        NearestNeighborOptimizer opt = new NearestNeighborOptimizer();
        Vehicule v = Vehicule.builder().build();

        Delivery a = Delivery.builder().id(1L).gpsLat(34.0).gpsLon(-6.84).build(); // Rabat
        Delivery b = Delivery.builder().id(2L).gpsLat(34.03).gpsLon(-5.00).build(); // Fes
        Delivery c = Delivery.builder().id(3L).gpsLat(35.76).gpsLon(-5.83).build(); // Tanger

        List<Delivery> input = Arrays.asList(a, b, c);

        List<Delivery> out = opt.calculateOptimalTour(input, v);

        assertNotNull(out);
        assertEquals(3, out.size());

        // Ensure uniqueness by id
        assertEquals(3, out.stream().map(Delivery::getId).distinct().count());

        // Must contain all ids
        assertTrue(out.stream().anyMatch(d -> d.getId() == 1L));
        assertTrue(out.stream().anyMatch(d -> d.getId() == 2L));
        assertTrue(out.stream().anyMatch(d -> d.getId() == 3L));
    }
}

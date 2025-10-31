package com.example.Livraison.service;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Vehicule;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClarkeWrightOptimizerTest {

    @Test
    void calculateOptimalTour_returnsEachDeliveryOnce_andNoDepotAtEnd() {
        ClarkeWrightOptimizer opt = new ClarkeWrightOptimizer();
        Vehicule v = Vehicule.builder().build();

        Delivery depot = Delivery.builder().gpsLat(34.0333).gpsLon(-5.0000).build();
        Delivery d1 = Delivery.builder().id(1L).gpsLat(34.0209).gpsLon(-6.8416).build(); // Rabat
        Delivery d2 = Delivery.builder().id(2L).gpsLat(33.5731).gpsLon(-7.5898).build(); // Casa
        Delivery d3 = Delivery.builder().id(3L).gpsLat(35.7595).gpsLon(-5.8339).build(); // Tanger

        List<Delivery> input = Arrays.asList(depot, d1, d2, d3);

        List<Delivery> out = opt.calculateOptimalTour(input, v);

        assertNotNull(out);
        assertFalse(out.isEmpty());

        assertNull(out.get(0).getId());
        assertNotNull(out.get(out.size() - 1).getId(), "Depot should not be appended at end");

        long uniqueClients = out.stream().filter(d -> d.getId() != null).map(Delivery::getId).distinct().count();
        assertEquals(3, uniqueClients);
    }
}

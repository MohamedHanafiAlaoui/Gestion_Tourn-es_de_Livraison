package com.example.Livraison.service;

import com.example.Livraison.dao.Repository.DeliveryRepository;
import com.example.Livraison.dao.Repository.TourRepository;
import com.example.Livraison.dao.Repository.WarehouseRepository;
import com.example.Livraison.dao.Repository.VehiculeRepository;
import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.dto.TourDTO;
import com.example.Livraison.mapper.TourMapper;
import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.Tour;
import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.Warehouse;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.TypeVehicule;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TourService {

    private final TourRepository tourRepository;
    private final DeliveryRepository deliveryRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TourOptimizer tourOptimizer;
    private final WarehouseRepository warehouseRepository;
    private static final Logger LOG = LoggerFactory.getLogger(TourService.class);

    public TourService(TourRepository tourRepository,
                       DeliveryRepository deliveryRepository,
                       VehiculeRepository vehiculeRepository,
                       TourOptimizer tourOptimizer,
                       WarehouseRepository warehouseRepository) {
        this.tourRepository = tourRepository;
        this.deliveryRepository = deliveryRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.tourOptimizer = tourOptimizer;
        this.warehouseRepository = warehouseRepository;
    }

    public List<TourDTO> findAll() {
        return tourRepository.findAll()
                .stream()
                .map(TourMapper::toDto)
                .collect(Collectors.toList());
    }

    public TourDTO findById(long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));
        return TourMapper.toDto(tour);
    }

    @Transactional
    public TourDTO create(TourDTO tourDTO) {
        Tour entity = TourMapper.toEntity(tourDTO);

        if (tourDTO.getWarehouseId() == null) {
            throw new IllegalStateException("Warehouse is required");
        }
        Warehouse warehouse = warehouseRepository.findById(tourDTO.getWarehouseId())
                .orElseThrow(() -> new IllegalStateException("Warehouse not found"));
        entity.setWarehouse(warehouse);

        if (tourDTO.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(tourDTO.getVehiculeId())
                    .orElseThrow(() -> new IllegalStateException("Vehicule not found"));

            if (!vehicule.getEtat().equals(EtatVehicule.DISPONIBLE)) {
                throw new IllegalStateException("Vehicule not available");
            }

            entity.setVehicule(vehicule);

            if (entity.getDate() == null) {
                throw new IllegalStateException("Date is required when assigning a vehicule");
            }

            boolean existsSameVehiculeSameDate = tourRepository
                    .existsByVehiculeIdAndDate(vehicule.getId(), entity.getDate());
            if (existsSameVehiculeSameDate) {
                throw new IllegalStateException("Vehicule already assigned to another tour on this date");
            }
        }

        if (tourDTO.getDeliveryIds() != null && !tourDTO.getDeliveryIds().isEmpty()) {
            List<Long> distinctIds = tourDTO.getDeliveryIds()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            List<Delivery> deliveries = deliveryRepository.findAllById(distinctIds);

            for (Delivery d : deliveries) {
                if (d.getTour() != null) {
                    throw new IllegalStateException("A delivery is already assigned to a tour and cannot be reassigned");
                }
                d.setTour(entity);
            }

            entity.setDeliveries(deliveries);

            if (entity.getVehicule() != null) {
                verifyVehiculeCapacity(entity.getVehicule(), deliveries);
            }
        }

        Tour saved = tourRepository.save(entity);
        return TourMapper.toDto(saved);
    }

    private void verifyVehiculeCapacity(Vehicule vehicule, List<Delivery> deliveries) {
        double totalPoids = deliveries.stream().mapToDouble(Delivery::getPoidsKg).sum();
        double totalVolume = deliveries.stream().mapToDouble(Delivery::getVolumeM3).sum();

        if (totalPoids > vehicule.getCapaciteMaxKg()) {
            throw new IllegalStateException("Poids total dépasse capacité véhicule");
        }

        if (totalVolume > vehicule.getCapaciteMaxM3()) {
            throw new IllegalStateException("Volume total dépasse capacité véhicule");
        }
    }

    public List<DeliveryDTO> getOptimizedTour(long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));

        if (tour.getWarehouse() == null) {
            throw new IllegalStateException("Tour must have a Warehouse for optimization");
        }

        LOG.info("Optimize tour id={} from warehouse lat={}, lon={}, vehiculeType={}", tourId,
                tour.getWarehouse().getGpsLat(), tour.getWarehouse().getGpsLong(),
                tour.getVehicule() != null ? tour.getVehicule().getType() : null);

        List<Delivery> distinctDeliveries = tour.getDeliveries()
                .stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Delivery::getId, d -> d, (a, b) -> a),
                        m -> new java.util.ArrayList<>(m.values())
                ));

        LOG.info("Input deliveries (distinct) count={} ids={}",
                distinctDeliveries.size(),
                distinctDeliveries.stream().map(Delivery::getId).collect(java.util.stream.Collectors.toList()));

        List<Delivery> optimized;
        boolean hasWarehouse = tour.getWarehouse() != null;
        if (hasWarehouse) {
            // GPS-only nearest neighbor starting from warehouse
            double curLat = tour.getWarehouse().getGpsLat();
            double curLon = tour.getWarehouse().getGpsLong();

            java.util.List<Delivery> remaining = new java.util.ArrayList<>(distinctDeliveries);
            java.util.List<Delivery> ordered = new java.util.ArrayList<>();
            while (!remaining.isEmpty()) {
                Delivery nearest = null;
                double minDist = Double.MAX_VALUE;
                for (Delivery d : remaining) {
                    double dx = curLat - d.getGpsLat();
                    double dy = curLon - d.getGpsLon();
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist < minDist) { minDist = dist; nearest = d; }
                }
                ordered.add(nearest);
                curLat = nearest.getGpsLat();
                curLon = nearest.getGpsLon();
                remaining.remove(nearest);
            }
            optimized = ordered;
        } else {
            // Fallback when no warehouse: keep previous vehicle-based behavior
            if (tour.getVehicule() != null && tour.getVehicule().getType() == TypeVehicule.CAMION) {
                optimized = new ClarkeWrightOptimizer().calculateOptimalTour(
                        distinctDeliveries,
                        tour.getVehicule()
                );
            } else {
                optimized = new NearestNeighborOptimizer().calculateOptimalTour(
                        distinctDeliveries,
                        tour.getVehicule()
                );
            }
        }

        LOG.info("Optimized order ids={}", optimized.stream().map(Delivery::getId)
                .collect(java.util.stream.Collectors.toList()));

        return optimized.stream()
                .map(DeliveryDTO::toDto)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(DeliveryDTO::getId, d -> d, (a, b) -> a),
                        m -> new java.util.ArrayList<>(m.values())
                ));
    }

    public double getTotalDistance(long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));

        if (tour.getWarehouse() == null) {
            throw new IllegalStateException("Tour must have a Warehouse for distance calculation");
        }

        boolean hasWarehouse = tour.getWarehouse() != null;
        List<Delivery> distinctDeliveries = tour.getDeliveries()
                .stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Delivery::getId, d -> d, (a, b) -> a),
                        m -> new java.util.ArrayList<>(m.values())
                ));

        // Determine the same ordering as in getOptimizedTour
        List<Delivery> ordered;
        if (tour.getVehicule() != null && tour.getVehicule().getType() == TypeVehicule.CAMION) {
            if (hasWarehouse) {
                Delivery depot = Delivery.builder()
                        .gpsLat(tour.getWarehouse().getGpsLat())
                        .gpsLon(tour.getWarehouse().getGpsLong())
                        .build();
                java.util.List<Delivery> withDepot = new java.util.ArrayList<>();
                withDepot.add(depot);
                withDepot.addAll(distinctDeliveries);
                java.util.List<Delivery> routed = new ClarkeWrightOptimizer().calculateOptimalTour(
                        withDepot,
                        tour.getVehicule()
                );
                ordered = routed.stream()
                        .filter(d -> d.getId() != null)
                        .collect(java.util.stream.Collectors.toList());
            } else {
                ordered = new ClarkeWrightOptimizer().calculateOptimalTour(
                        distinctDeliveries,
                        tour.getVehicule()
                );
            }
        } else {
            if (hasWarehouse) {
                java.util.List<Delivery> remaining = new java.util.ArrayList<>(distinctDeliveries);
                java.util.List<Delivery> temp = new java.util.ArrayList<>();
                double curLat = tour.getWarehouse().getGpsLat();
                double curLon = tour.getWarehouse().getGpsLong();
                while (!remaining.isEmpty()) {
                    Delivery nearest = null;
                    double minDist = Double.MAX_VALUE;
                    for (Delivery d : remaining) {
                        double dx = curLat - d.getGpsLat();
                        double dy = curLon - d.getGpsLon();
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        if (dist < minDist) { minDist = dist; nearest = d; }
                    }
                    temp.add(nearest);
                    curLat = nearest.getGpsLat();
                    curLon = nearest.getGpsLon();
                    remaining.remove(nearest);
                }
                ordered = temp;
            } else {
                ordered = new NearestNeighborOptimizer().calculateOptimalTour(
                        distinctDeliveries,
                        tour.getVehicule()
                );
            }
        }

        // Compute distance Warehouse -> D1 -> ... -> Warehouse if warehouse exists
        if (hasWarehouse && !ordered.isEmpty()) {
            double total = 0.0;
            double wLat = tour.getWarehouse().getGpsLat();
            double wLon = tour.getWarehouse().getGpsLong();

            // Warehouse to first
            double dxStart = wLat - ordered.get(0).getGpsLat();
            double dyStart = wLon - ordered.get(0).getGpsLon();
            total += Math.sqrt(dxStart * dxStart + dyStart * dyStart);

            // Between deliveries
            for (int i = 0; i < ordered.size() - 1; i++) {
                double dx = ordered.get(i).getGpsLat() - ordered.get(i + 1).getGpsLat();
                double dy = ordered.get(i).getGpsLon() - ordered.get(i + 1).getGpsLon();
                total += Math.sqrt(dx * dx + dy * dy);
            }

            // Last back to warehouse
            double dxEnd = ordered.get(ordered.size() - 1).getGpsLat() - wLat;
            double dyEnd = ordered.get(ordered.size() - 1).getGpsLon() - wLon;
            total += Math.sqrt(dxEnd * dxEnd + dyEnd * dyEnd);

            LOG.info("Computed total distance for tour id={} = {} (warehouse start/end)", tourId, total);
            return total;
        }

        // Fallback if no warehouse
        return tourOptimizer.getTotalDistance(ordered);
    }
}

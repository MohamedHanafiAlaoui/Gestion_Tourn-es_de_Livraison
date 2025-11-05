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
import com.example.Livraison.model.enums.Status;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

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
        return findAll("id", "asc");
    }

    public List<TourDTO> findAll(String sortBy, String direction) {
        String safeSortBy = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        Sort sort = (direction != null && direction.equalsIgnoreCase("desc"))
                ? Sort.by(Sort.Order.desc(safeSortBy))
                : Sort.by(Sort.Order.asc(safeSortBy));
        return tourRepository.findAll(sort)
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
                d.setStatus(Status.ASSIGNED);
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

    @Transactional
    public TourDTO start(long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));

        if (tour.getDeliveries() == null || tour.getDeliveries().isEmpty()) {
            throw new IllegalStateException("Cannot start a tour without deliveries");
        }

        for (Delivery d : tour.getDeliveries()) {
            if (d.getStatus() == Status.ASSIGNED || d.getStatus() == Status.PENDING) {
                d.setStatus(Status.IN_TRANSIT);
            }
        }
        deliveryRepository.saveAll(tour.getDeliveries());
        return TourMapper.toDto(tour);
    }

    @Transactional
    public TourDTO complete(long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));

        if (tour.getDeliveries() != null) {
            for (Delivery d : tour.getDeliveries()) {
                if (d.getStatus() == Status.IN_TRANSIT || d.getStatus() == Status.ASSIGNED) {
                    d.setStatus(Status.DELIVERED);
                }
            }
            deliveryRepository.saveAll(tour.getDeliveries());
        }
        return TourMapper.toDto(tour);
    }

    @Transactional
    public TourDTO cancel(long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));

        if (tour.getDeliveries() != null) {
            for (Delivery d : tour.getDeliveries()) {
                if (d.getStatus() != Status.DELIVERED) {
                    d.setStatus(Status.PENDING);
                    d.setTour(null);
                }
            }
            deliveryRepository.saveAll(tour.getDeliveries());
            tour.setDeliveries(
                    tour.getDeliveries().stream().filter(del -> del.getTour() != null).collect(Collectors.toList())
            );
        }
        return TourMapper.toDto(tour);
    }

    public List<DeliveryDTO> getOptimizedTour(long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalStateException("Tour not found"));

        if (tour.getWarehouse() == null) {
            throw new IllegalStateException("Tour must have a Warehouse for optimization");
        }

        if (tour.getDeliveries() == null || tour.getDeliveries().isEmpty()) {
            LOG.warn("Optimize tour id={} has no deliveries; returning empty route", tourId);
            return Collections.emptyList();
        }

        LOG.info("Optimize tour id={} from warehouse lat={}, lon={}, vehiculeType={}", tourId,
                tour.getWarehouse().getGpsLat(), tour.getWarehouse().getGpsLong(),
                tour.getVehicule() != null ? tour.getVehicule().getType() : null);

        List<Delivery> distinctDeliveries = tour.getDeliveries()
                .stream()
                .filter(d -> d != null && d.getId() != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Delivery::getId, d -> d, (a, b) -> a),
                        m -> new ArrayList<>(m.values())
                ));

        if (distinctDeliveries.isEmpty()) {
            LOG.warn("Optimize tour id={} has no valid deliveries with IDs; returning empty route", tourId);
            return Collections.emptyList();
        }

        LOG.info("Input deliveries (distinct) count={} ids={}",
                distinctDeliveries.size(),
                distinctDeliveries.stream().map(Delivery::getId).collect(Collectors.toList()));

        LOG.info("Input deliveries coords={} ",
                distinctDeliveries.stream()
                        .map(d -> String.format("%d:(%f,%f)", d.getId(), d.getGpsLat(), d.getGpsLon()))
                        .collect(Collectors.toList()));

        List<Delivery> optimized;

        final Delivery depot = Delivery.builder()
                .gpsLat(tour.getWarehouse().getGpsLat())
                .gpsLon(tour.getWarehouse().getGpsLong())
                .build();

        List<Delivery> input = new ArrayList<>(distinctDeliveries);
        List<Delivery> withDepot = new ArrayList<>();
        withDepot.add(depot);
        withDepot.addAll(input);
        input = withDepot;


        optimized = tourOptimizer.calculateOptimalTour(
                input,
                tour.getVehicule()
        );



        if (depot != null) {
            optimized = optimized.stream()
                    .filter(d -> d != depot)
                    .collect(Collectors.toList());
        }



        LOG.info("Optimized order ids={}", optimized.stream().map(Delivery::getId)
                .collect(Collectors.toList()));
        LOG.info("Optimized order coords={}", optimized.stream()
                .map(d -> d.getId() != null ? String.format("%d:(%f,%f)", d.getId(), d.getGpsLat(), d.getGpsLon()) : String.format("null:(%f,%f)", d.getGpsLat(), d.getGpsLon()))
                .collect(Collectors.toList()));

        return optimized.stream()
        .map(DeliveryDTO::toDto)
        .collect(Collectors.toList());

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
                        m -> new ArrayList<>(m.values())
                ));

        List<Delivery> ordered;

            ordered = tourOptimizer.calculateOptimalTour(
                    distinctDeliveries,
                    tour.getVehicule());


        if (hasWarehouse && !ordered.isEmpty()) {
            double total = 0.0;
            double wLat = tour.getWarehouse().getGpsLat();
            double wLon = tour.getWarehouse().getGpsLong();

            double dxStart = wLat - ordered.get(0).getGpsLat();
            double dyStart = wLon - ordered.get(0).getGpsLon();
            total += Math.sqrt(dxStart * dxStart + dyStart * dyStart);

            for (int i = 0; i < ordered.size() - 1; i++) {
                double dx = ordered.get(i).getGpsLat() - ordered.get(i + 1).getGpsLat();
                double dy = ordered.get(i).getGpsLon() - ordered.get(i + 1).getGpsLon();
                total += Math.sqrt(dx * dx + dy * dy);
            }

            double dxEnd = ordered.get(ordered.size() - 1).getGpsLat() - wLat;
            double dyEnd = ordered.get(ordered.size() - 1).getGpsLon() - wLon;
            total += Math.sqrt(dxEnd * dxEnd + dyEnd * dyEnd);

            LOG.info("Computed total distance for tour id={} = {} (warehouse start/end)", tourId, total);
            return total;
        }

        return tourOptimizer.getTotalDistance(ordered);
    }

}

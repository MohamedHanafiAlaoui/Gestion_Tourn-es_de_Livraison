package com.example.Livraison.controller;

import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.dto.TourDTO;
import com.example.Livraison.service.TourService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public List<TourDTO> getAllTours() {
        return tourService.findAll();
    }

    @GetMapping("/{id}")
    public TourDTO getTourById(@PathVariable long id) {
        return tourService.findById(id);
    }

    @PostMapping
    public TourDTO createTour(@RequestBody TourDTO tourDTO) {
        return tourService.create(tourDTO);
    }

    @GetMapping("/{id}/optimize")
    public List<DeliveryDTO> getOptimizedTour(@PathVariable long id) {
        return tourService.getOptimizedTour(id);
    }

    @GetMapping("/{id}/distance")
    public double getTotalDistance(@PathVariable long id) {
        return tourService.getTotalDistance(id);
    }

    @PostMapping("/{id}/start")
    public TourDTO startTour(@PathVariable long id) {
        return tourService.start(id);
    }

    @PostMapping("/{id}/complete")
    public TourDTO completeTour(@PathVariable long id) {
        return tourService.complete(id);
    }

    @PostMapping("/{id}/cancel")
    public TourDTO cancelTour(@PathVariable long id) {
        return tourService.cancel(id);
    }
}

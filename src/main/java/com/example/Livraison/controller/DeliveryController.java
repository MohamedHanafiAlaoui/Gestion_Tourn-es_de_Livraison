package com.example.Livraison.controller;

import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.model.enums.Status;
import com.example.Livraison.service.DeliveryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryService.findAll();
    }

    @GetMapping("/{id}")
    public DeliveryDTO getDeliveryById(@PathVariable long id) {
        return deliveryService.findById(id);
    }

    @PostMapping
    public DeliveryDTO createDelivery(@RequestBody DeliveryDTO dto) {
        return deliveryService.create(dto);
    }

    @PutMapping("/{id}")
    public DeliveryDTO updateDelivery(@PathVariable long id, @RequestBody DeliveryDTO dto) {
        return deliveryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDelivery(@PathVariable long id) {
        deliveryService.delete(id);
    }
}

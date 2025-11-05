package com.example.Livraison.controller;

import com.example.Livraison.dto.VehiculeDTO;
import com.example.Livraison.service.VehiculeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> getAllVehicules() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehiculeById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VehiculeDTO> createVehicule(@RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(vehiculeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> updateVehicule(@PathVariable Long id, @RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(vehiculeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        vehiculeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/Type")
    public ResponseEntity<List<VehiculeDTO>> findByTypeSortWeight()
    {
        return ResponseEntity.ok(vehiculeService.findByTypeSortWeight());
    }

    @GetMapping("/Types")
    public ResponseEntity<List<VehiculeDTO>> findByType()
    {
        return ResponseEntity.ok(vehiculeService.findByType());
    }

}

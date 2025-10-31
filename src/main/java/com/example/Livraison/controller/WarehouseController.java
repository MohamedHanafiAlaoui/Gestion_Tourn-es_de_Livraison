package com.example.Livraison.controller;

import com.example.Livraison.dto.WarehouseDTO;
import com.example.Livraison.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseService.findAll();
    }

    @GetMapping("/{id}")
    public WarehouseDTO getWarehouseById(@PathVariable long id) {
        return warehouseService.findById(id);
    }

    @PostMapping
    public WarehouseDTO createWarehouse(@RequestBody WarehouseDTO dto) {
        return warehouseService.create(dto);
    }

    @PutMapping("/{id}")
    public WarehouseDTO updateWarehouse(@PathVariable long id, @RequestBody WarehouseDTO dto) {
        return warehouseService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteWarehouse(@PathVariable long id) {
        warehouseService.delete(id);
    }
}

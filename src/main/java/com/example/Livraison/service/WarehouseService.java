package com.example.Livraison.service;

import com.example.Livraison.dao.Repository.WarehouseRepository;
import com.example.Livraison.dto.VehiculeDTO;
import com.example.Livraison.dto.WarehouseDTO;
import com.example.Livraison.mapper.WarehouseMapper;
import com.example.Livraison.model.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseService
{
    private final WarehouseRepository  warehouseRepository;
    public WarehouseService(WarehouseRepository warehouseRepository)
    {
        this.warehouseRepository = warehouseRepository;
    }

    public List<WarehouseDTO> findAll()
    {
        return warehouseRepository.findAll()
                .stream()
                .map(WarehouseMapper::toDto)
                .collect(Collectors.toList());

    }

    public WarehouseDTO findById(Long id)
    {
        Warehouse  warehouse = warehouseRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Warehouse with id "+id+" not found"));
        return WarehouseMapper.toDto(warehouse);

    }

    public  WarehouseDTO create(WarehouseDTO warehouseDTO)
    {
        Warehouse entity = WarehouseMapper.toEntity(warehouseDTO);
        Warehouse   warehouse = warehouseRepository.save(entity);
        return WarehouseMapper.toDto(warehouse);
    }

    public WarehouseDTO update(long id, WarehouseDTO warehouseDTO) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse with id " + id + " not found"));

        existing.setAdresse(warehouseDTO.getAdresse());
        existing.setGpsLat(warehouseDTO.getGpsLat());
        existing.setGpsLong(warehouseDTO.getGpsLong());
        existing.setHoraireOuverture(warehouseDTO.getHoraireOuverture());
        existing.setHoraireFermeture(warehouseDTO.getHoraireFermeture());

        Warehouse updated = warehouseRepository.save(existing);
        return WarehouseMapper.toDto(updated);
    }



    public  void delete(long id)
    {
        if (!warehouseRepository.existsById(id))
            {
            throw new IllegalArgumentException("Warehouse with id "+id+" not found");
            }
        warehouseRepository.deleteById(id);
    }






}

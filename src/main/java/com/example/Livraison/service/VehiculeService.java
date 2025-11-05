package com.example.Livraison.service;

import com.example.Livraison.dao.Repository.VehiculeRepository;
import com.example.Livraison.dto.VehiculeDTO;
import com.example.Livraison.mapper.VehiculeMapper;
import com.example.Livraison.model.Vehicule;
import com.example.Livraison.model.enums.EtatVehicule;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VehiculeService {

    private final VehiculeRepository vehcrepository;

    public VehiculeService(VehiculeRepository vehcrepository)
    {
        this.vehcrepository=vehcrepository;
    }

    public List<VehiculeDTO> findAll()
    {
        return  vehcrepository.findAll()
                .stream()
                .map(VehiculeDTO::toDto)
                .collect(Collectors.toList());
    }


    public VehiculeDTO findById(Long id)
    {
            Vehicule vehicule = vehcrepository.findById(id)
                    .orElseThrow(()-> new IllegalStateException("Vehicule with id " + id + " not found"));
        return VehiculeMapper.toDto(vehicule);
    }

    public VehiculeDTO create(VehiculeDTO dto)
    {
        Vehicule entity = VehiculeMapper.toEntity(dto);
        Vehicule vehicule = vehcrepository.save(entity);

        return VehiculeMapper.toDto(vehicule);
    }

    public VehiculeDTO update(Long id,VehiculeDTO dto)
    {
        Vehicule existing = vehcrepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Vehicule not found with ID: " + id));
        existing.setType(dto.getType());
        existing.setEtat(dto.getEtat());
        existing.setCapaciteMaxKg(dto.getCapaciteMaxKg());
        existing.setCapaciteMaxM3(dto.getCapaciteMaxM3());

        Vehicule updated = vehcrepository.save(existing);
        return VehiculeMapper.toDto(updated);
    }

    public void delete(Long id)
    {
        if (!vehcrepository.existsById(id)) {
            throw new IllegalStateException("Vehicule not found with ID: " + id);
        }
        vehcrepository.deleteById(id);
    }

    public  List<VehiculeDTO> findByTypeSortWeight()
    {
        List<Vehicule>  vehicules = vehcrepository.findVehiculeByCapaciteMaxKg();

        return vehicules.stream().map(VehiculeDTO::toDto)
            .collect(Collectors.toList());
    }


    public  List<VehiculeDTO> findByType()
    {
        List<Vehicule>  vehicules = vehcrepository.findByOrderByCapaciteMaxKg();

        return vehicules.stream().map(VehiculeDTO::toDto).collect(Collectors.toList());
    }




}

package com.example.Livraison.service;

import com.example.Livraison.dao.Repository.DeliveryRepository;
import com.example.Livraison.dto.DeliveryDTO;
import com.example.Livraison.mapper.DeliveryMapper;
import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.enums.EtatVehicule;
import com.example.Livraison.model.enums.Status;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryService {

    private final DeliveryRepository deliveryRepository;


    public   DeliveryService(DeliveryRepository    deliveryRepository)
    {
        this.deliveryRepository = deliveryRepository;
    }

    public List<DeliveryDTO> findAll()
    {
        return deliveryRepository.findAll()
                .stream()
                .map(DeliveryDTO::toDto)
                .collect(Collectors.toList());
    }

    public  DeliveryDTO findById(long id)
    {
        Delivery  delivery  = deliveryRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("delivery with id " + id + " not found"));

        return DeliveryMapper.toDto(delivery);
    }

    public  DeliveryDTO create(DeliveryDTO dto)
    {
        Delivery entity = DeliveryMapper.toEntity(dto);
        entity.setStatus(Status.PENDING);

        Delivery delivery = deliveryRepository.save(entity);

        return DeliveryMapper.toDto(delivery);
    }

    public  DeliveryDTO update(long id,DeliveryDTO dto)
    {

        Delivery  existing = deliveryRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("delivery with id " + id + " not found"));

        existing.setStatus(dto.getStatus());
        existing.setAdresse(dto.getAdresse());
        existing.setGpsLat(dto.getGpsLat());
        existing.setGpsLon(dto.getGpsLon());
        existing.setPoidsKg(dto.getPoidsKg());
        existing.setVolumeM3(dto.getVolumeM3());
        existing.setCreneauPref(dto.getCreneauPref());
        Delivery delivery = deliveryRepository.save(existing);
        return DeliveryMapper.toDto(delivery);

    }


    public  void delete(long id)
    {
        if (!deliveryRepository.existsById(id)) {
            throw new IllegalStateException("Delivery not found with ID: " + id);
        }
        deliveryRepository.deleteById(id);
    }





}

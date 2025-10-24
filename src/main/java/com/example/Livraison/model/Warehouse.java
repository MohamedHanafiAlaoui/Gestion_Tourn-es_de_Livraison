package com.example.Livraison.model;


import com.example.Livraison.dto.WarehouseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "warehouse")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adresse;
    private double gpsLat;
    private double gpsLong;
    private String horaireOuverture;
    private String horaireFermeture;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<Tour> tours;


    public static Warehouse fromDTO(WarehouseDTO warehouseDTO)
    {
        return
                Warehouse.builder()
                        .id(warehouseDTO.getId())
                        .adresse(warehouseDTO.getAdresse())
                        .gpsLat(warehouseDTO.getGpsLat())
                        .gpsLong(warehouseDTO.getGpsLong())
                        .horaireFermeture(warehouseDTO.getHoraireFermeture())
                        .horaireOuverture(warehouseDTO.getHoraireOuverture())
                        .build();
    }

}

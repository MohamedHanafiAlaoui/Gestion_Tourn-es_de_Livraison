package com.example.Livraison.model;

import com.example.Livraison.dto.TourDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)

    private List<Delivery> deliveries;

    public static Tour  fromDTO(TourDTO tourDTO)
    {
        return Tour.builder()
                .id(tourDTO.getId())
                .date(tourDTO.getDate())
                .vehicule(tourDTO.getVehiculeId() != null ? Vehicule.builder().id(tourDTO.getVehiculeId()).build() : null)
                .warehouse(tourDTO.getWarehouseId() != null ? Warehouse.builder().id(tourDTO.getWarehouseId()).build() : null)
                .build();

    }



}

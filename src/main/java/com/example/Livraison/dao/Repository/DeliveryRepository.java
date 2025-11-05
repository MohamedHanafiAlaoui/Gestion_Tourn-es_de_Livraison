package com.example.Livraison.dao.Repository;

import com.example.Livraison.model.Delivery;
import com.example.Livraison.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
@Query(value ="select * from delivery   where status = :status" , nativeQuery = true)
    List<Delivery> findByStatus(@Param("status") Status status);
}

package com.example.aston.delivery.repository;

import com.example.aston.delivery.model.CourierDelivery;
import com.example.aston.delivery.model.Delivery;
import com.example.aston.delivery.model.ParcelLockerDelivery;
import com.example.aston.delivery.model.PostDelivery;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    @Query("FROM PostDelivery")
    List<PostDelivery> findAllPostDeliveries();

    @Query("FROM CourierDelivery")
    List<CourierDelivery> findAllCourierDeliveries();

    @Query("FROM ParcelLockerDelivery")
    List<ParcelLockerDelivery> findAllParcelLockerDeliveries();
}
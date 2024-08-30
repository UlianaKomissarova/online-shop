package aston.delivery.repository;

import aston.delivery.model.*;
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
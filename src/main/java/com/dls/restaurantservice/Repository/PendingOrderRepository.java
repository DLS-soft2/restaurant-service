package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Document.PendingOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingOrderRepository extends MongoRepository<PendingOrder, String> {

    Optional<PendingOrder> findByOrderId(String orderId);

    boolean existsByOrderId(String orderId);

    List<PendingOrder> findByRestaurantIdAndStatus(String restaurantId, String status);
}

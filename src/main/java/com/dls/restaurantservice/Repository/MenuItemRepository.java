package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Document.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByRestaurant_RestaurantId(String restaurantId);
}
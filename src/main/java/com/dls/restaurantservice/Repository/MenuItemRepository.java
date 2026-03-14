package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_RestaurantId(Long restaurantId);
}

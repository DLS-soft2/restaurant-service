package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository  extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByNameIgnoreCase(String name);
    List<Restaurant> findByIsAvailable(Boolean isAvailable);
    List<Restaurant> findByIsOpen(Boolean isOpen);
    List<Restaurant> findByAddressContainingIgnoreCase(String location);
}

package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Document.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    Optional<Restaurant> findByNameIgnoreCase(String name);

    List<Restaurant> findByIsAvailable(Boolean isAvailable);

    List<Restaurant> findByIsOpen(Boolean isOpen);

    List<Restaurant> findByAddressContainingIgnoreCase(String location);

    Page<Restaurant> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    Page<Restaurant> findByIsOpen(Boolean isOpen, Pageable pageable);

    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'address': { $regex: ?0, $options: 'i' } } ] }")
    Page<Restaurant> search(String query, Pageable pageable);
}
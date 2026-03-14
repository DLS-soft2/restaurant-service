package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository  extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByNameIgnoreCase(String name);
    List<Restaurant> findByIsAvailable(Boolean isAvailable);
    List<Restaurant> findByIsOpen(Boolean isOpen);
    List<Restaurant> findByAddressContainingIgnoreCase(String location);


    // Pagination
    @NonNull
    Page<Restaurant> findAll(@NonNull Pageable pageable);
    @NonNull
    Page<Restaurant> findByIsAvailable(Boolean isAvailable, @NonNull Pageable pageable);
    @NonNull
    Page<Restaurant> findByIsOpen(Boolean isOpen, @NonNull Pageable pageable);

    // Fritekst søgning på navn, beskrivelse og kategori
    @Query("SELECT r FROM Restaurant r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.address) LIKE LOWER(CONCAT('%', :query, '%'))")
    @NonNull
    Page<Restaurant> search(@Param("query") String query, @NonNull Pageable pageable);
}

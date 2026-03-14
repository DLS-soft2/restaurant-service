package com.dls.restaurantservice.Service;

import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Entity.Restaurant;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<RestaurantResponse> GetAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantResponse::new)
                .collect(Collectors.toList());
    }

    public RestaurantResponse GetRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        return new RestaurantResponse(restaurant);
    }

    public RestaurantResponse GetRestaurantByName(String name) {
        Restaurant restaurant = restaurantRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with name: " + name));
        return new RestaurantResponse(restaurant);
    }

    public List<RestaurantResponse> GetRestaurantsByAvailability(Boolean isAvailable) {
        return restaurantRepository.findByIsAvailable(isAvailable).stream()
                .map(RestaurantResponse::new)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> GetRestaurantsByOpenStatus(Boolean isOpen) {
        return restaurantRepository.findByIsOpen(isOpen).stream()
                .map(RestaurantResponse::new)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> GetRestaurantsByLocation(String location) {
        return restaurantRepository.findByAddressContainingIgnoreCase(location).stream()
                .map(RestaurantResponse::new)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> GetAvailableRestaurants() {
        return restaurantRepository.findByIsAvailable(true).stream()
                .map(RestaurantResponse::new)
                .collect(Collectors.toList());
    }

    public RestaurantResponse AddRestaurant(RestaurantRequest restaurantRequest) {
        validateAddress(restaurantRequest.getAddress());
        validateDescription(restaurantRequest.getDescription());
        validateEmail(restaurantRequest.getEmail());
        validateIsAvailable(restaurantRequest.getIsAvailable() != null ? restaurantRequest.getIsAvailable().toString() : null);
        validateIsOpen(restaurantRequest.getIsOpen() != null ? restaurantRequest.getIsOpen().toString() : null);
        validateName(restaurantRequest.getName());
        validateOpeningHours(restaurantRequest.getOpeningHours());
        validatePhoneNumber(restaurantRequest.getPhoneNumber());

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantRequest.getName());
        restaurant.setAddress(restaurantRequest.getAddress());
        restaurant.setPhoneNumber(restaurantRequest.getPhoneNumber());
        restaurant.setEmail(restaurantRequest.getEmail());
        restaurant.setDescription(restaurantRequest.getDescription());
        restaurant.setOpeningHours(restaurantRequest.getOpeningHours());
        restaurant.setIsOpen(restaurantRequest.getIsOpen());
        restaurant.setIsAvailable(restaurantRequest.getIsAvailable());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return new RestaurantResponse(savedRestaurant);
    }

    public RestaurantResponse UpdateRestaurant(Long id, @Valid RestaurantRequest restaurantRequest) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        validateAddress(restaurantRequest.getAddress());
        validateDescription(restaurantRequest.getDescription());
        validateEmail(restaurantRequest.getEmail());
        validateIsAvailable(restaurantRequest.getIsAvailable() != null ? restaurantRequest.getIsAvailable().toString() : null);
        validateIsOpen(restaurantRequest.getIsOpen() != null ? restaurantRequest.getIsOpen().toString() : null);
        validateName(restaurantRequest.getName());
        validateOpeningHours(restaurantRequest.getOpeningHours());
        validatePhoneNumber(restaurantRequest.getPhoneNumber());

        restaurant.setName(restaurantRequest.getName());
        restaurant.setAddress(restaurantRequest.getAddress());
        restaurant.setPhoneNumber(restaurantRequest.getPhoneNumber());
        restaurant.setEmail(restaurantRequest.getEmail());
        restaurant.setDescription(restaurantRequest.getDescription());
        restaurant.setOpeningHours(restaurantRequest.getOpeningHours());
        restaurant.setIsOpen(restaurantRequest.getIsOpen());
        restaurant.setIsAvailable(restaurantRequest.getIsAvailable());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return new RestaurantResponse(updatedRestaurant);
    }

    public void DeleteRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        restaurantRepository.delete(restaurant);
    }

    /* Validation methods */

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Restaurant name must be between 2 and 100 characters");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (address.length() < 5 || address.length() > 200) {
            throw new IllegalArgumentException("Address must be between 5 and 200 characters");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            if (!phoneNumber.matches("\\+?[0-9]{7,15}")) {
                throw new IllegalArgumentException("Phone number must be between 7 and 15 digits and can start with +");
            }
        }
    }

    private void validateEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("Email should be valid");
            }
            if (email.length() < 5 || email.length() > 70) {
                throw new IllegalArgumentException("Email must be between 5 and 70 characters");
            }
        }
    }

    private void validateOpeningHours(String openingHours) {
        if (openingHours != null && !openingHours.trim().isEmpty()) {
            if (openingHours.length() < 5 || openingHours.length() > 100) {
                throw new IllegalArgumentException("Opening hours must be between 5 and 100 characters");
            }
        }
    }

    private void validateDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            if (description.length() < 10 || description.length() > 1000) {
                throw new IllegalArgumentException("Description must be between 10 and 1000 characters");
            }
        }
    }

    private void validateIsOpen(String isOpen) {
        if (isOpen == null || isOpen.trim().isEmpty()) {
            throw new IllegalArgumentException("Open status is required");
        }
        if (!isOpen.equalsIgnoreCase("true") && !isOpen.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Open status must be true or false");
        }
    }

    private void validateIsAvailable(String isAvailable) {
        if (isAvailable == null || isAvailable.trim().isEmpty()) {
            throw new IllegalArgumentException("Availability status is required");
        }
        if (!isAvailable.equalsIgnoreCase("true") && !isAvailable.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Availability status must be true or false");
        }
    }
}
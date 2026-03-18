package com.dls.restaurantservice.DTO;

import com.dls.restaurantservice.Document.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantResponse {
    private String restaurantId;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
    private String openingHours;
    private Boolean isOpen;
    private Boolean isAvailable;

    public RestaurantResponse(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.email = restaurant.getEmail();
        this.description = restaurant.getDescription();
        this.openingHours = restaurant.getOpeningHours();
        this.isOpen = restaurant.getIsOpen();
        this.isAvailable = restaurant.getIsAvailable();
    }
}
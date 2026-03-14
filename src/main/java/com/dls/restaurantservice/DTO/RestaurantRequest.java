package com.dls.restaurantservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
    private String openingHours;
    private Boolean isOpen;
    private Boolean isAvailable;
}

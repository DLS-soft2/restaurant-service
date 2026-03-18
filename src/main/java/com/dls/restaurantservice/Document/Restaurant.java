package com.dls.restaurantservice.Document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String restaurantId;

    private String name;
    private String address;
    private String phoneNumber;

    @Indexed(unique = true)
    private String email;

    private String description;
    private String openingHours;
    private Boolean isOpen;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
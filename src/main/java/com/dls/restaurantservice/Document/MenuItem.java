package com.dls.restaurantservice.Document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "menu_items")
public class MenuItem {

    @Id
    private String menuItemId;

    private String name;
    private String description;
    private Double price;

    @DBRef
    private Restaurant restaurant;
}
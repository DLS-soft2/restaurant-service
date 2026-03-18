package com.dls.restaurantservice.DTO;

import com.dls.restaurantservice.Document.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemResponse {
    private String menuItemId;
    private String name;
    private String description;
    private Double price;
    private String restaurantId;

    public MenuItemResponse(MenuItem menuItem) {
        this.menuItemId = menuItem.getMenuItemId();
        this.name = menuItem.getName();
        this.description = menuItem.getDescription();
        this.price = menuItem.getPrice();
        this.restaurantId = menuItem.getRestaurant().getRestaurantId();
    }
}
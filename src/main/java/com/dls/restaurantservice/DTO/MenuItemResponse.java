package com.dls.restaurantservice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemResponse {
    private Long menuItemId;
    private String name;
    private String description;
    private Double price;
    private Long restaurantId;
}

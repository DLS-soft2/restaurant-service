package com.dls.restaurantservice.Controller;

import com.dls.restaurantservice.DTO.MenuItemRequest;
import com.dls.restaurantservice.DTO.MenuItemResponse;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Service.MenuItemService;
import com.dls.restaurantservice.Service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-item")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public List<MenuItemResponse> getMenuItems() {
        return menuItemService.getAllMenuItems();
    }

    @GetMapping("/{menuItemId}")
    public MenuItemResponse getMenuItemById(@PathVariable Long menuItemId) {
        return menuItemService.getMenuItemById(menuItemId);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> getMenuItemsByRestaurantId(@PathVariable Long restaurantId) {
        return menuItemService.getMenuItemsByRestaurantId(restaurantId);
    }

    @PostMapping("/add")
    public MenuItemResponse addMenuItem(@RequestBody MenuItemRequest menuItemRequest) {
        return menuItemService.addMenuItem(menuItemRequest);
    }

    @PutMapping("/update/{menuItemId}")
    public MenuItemResponse updateMenuItem(@PathVariable Long menuItemId, @RequestBody MenuItemRequest menuItemRequest) {
        return menuItemService.updateMenuItem(menuItemId, menuItemRequest);
    }

    @DeleteMapping("/delete/{menuItemId}")
    public void deleteMenuItem(@PathVariable Long menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
    }

}

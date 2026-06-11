package com.dls.restaurantservice.Controller;

import com.dls.authlib.Permission;
import com.dls.authlib.RequirePermission;
import com.dls.restaurantservice.DTO.MenuItemRequest;
import com.dls.restaurantservice.DTO.MenuItemResponse;
import com.dls.restaurantservice.Service.MenuItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/restaurants/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    @RequirePermission(Permission.MENU_READ)
    public List<MenuItemResponse> getMenuItems() {
        return menuItemService.getAllMenuItems();
    }

    @GetMapping("/{menuItemId}")
    @RequirePermission(Permission.MENU_READ)
    public MenuItemResponse getMenuItemById(@PathVariable String menuItemId) {
        return menuItemService.getMenuItemById(menuItemId);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @RequirePermission(Permission.MENU_READ)
    public List<MenuItemResponse> getMenuItemsByRestaurantId(@PathVariable String restaurantId) {
        return menuItemService.getMenuItemsByRestaurantId(restaurantId);
    }

    @PostMapping
    @RequirePermission(Permission.MENU_CREATE)
    public MenuItemResponse addMenuItem(@RequestBody MenuItemRequest menuItemRequest) {
        return menuItemService.addMenuItem(menuItemRequest);
    }

    @PutMapping("/{menuItemId}")
    @RequirePermission(Permission.MENU_UPDATE)
    public MenuItemResponse updateMenuItem(@PathVariable String menuItemId, @RequestBody MenuItemRequest menuItemRequest) {
        return menuItemService.updateMenuItem(menuItemId, menuItemRequest);
    }

    @DeleteMapping("/{menuItemId}")
    @RequirePermission(Permission.MENU_DELETE)
    public void deleteMenuItem(@PathVariable String menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
    }
}

package com.dls.restaurantservice.Service;

import com.dls.restaurantservice.DTO.MenuItemRequest;
import com.dls.restaurantservice.DTO.MenuItemResponse;
import com.dls.restaurantservice.Entity.MenuItem;
import com.dls.restaurantservice.Entity.Restaurant;
import com.dls.restaurantservice.Repository.MenuItemRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<MenuItemResponse> getAllMenuItems(){
        return menuItemRepository.findAll().stream().map(MenuItemResponse::new).collect(Collectors.toList());
    }

    public MenuItemResponse getMenuItemById(Long menuItemId){
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new RuntimeException("Menu item not found with id: " + menuItemId));
        return new MenuItemResponse(menuItem);
    }

    public List<MenuItemResponse> getMenuItemsByRestaurantId(Long restaurantId){
        restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        List<MenuItem> menuItems = menuItemRepository.findByRestaurant_RestaurantId(restaurantId);
        if (menuItems.isEmpty()) {
            throw new RuntimeException("No menu items found for restaurant with id: " + restaurantId);
        }
        return menuItems.stream().map(MenuItemResponse::new).collect(Collectors.toList());
    }

    public MenuItemResponse addMenuItem(MenuItemRequest menuItemRequest){
        var restaurant = restaurantRepository.findById(menuItemRequest.getRestaurantId())
            .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + menuItemRequest.getRestaurantId()));

        validateName(menuItemRequest.getName());
        validateDescription(menuItemRequest.getDescription());
        validatePrice(menuItemRequest.getPrice());
        validateRestaurant(restaurant);



        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemRequest.getName());
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setRestaurant(restaurant);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return new MenuItemResponse(savedMenuItem);
    }

    public MenuItemResponse updateMenuItem(Long id, @Valid MenuItemRequest menuItemRequest){
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        Restaurant restaurant = restaurantRepository.findById(menuItemRequest.getRestaurantId())
            .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + menuItemRequest.getRestaurantId()));

        menuItem.setName(menuItemRequest.getName());
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setRestaurant(restaurant);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return new MenuItemResponse(updatedMenuItem);
    }

    public MenuItemResponse deleteMenuItem(Long id){
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        menuItemRepository.delete(menuItem);
        return new MenuItemResponse(menuItem);
    }

    /* Validation methods */

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Menu item name is required");
        }

        if (name.length() > 100) {
        throw new RuntimeException("Menu item name must be less than 100 characters");}
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new RuntimeException("Menu item description must be less than 500 characters");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new RuntimeException("Menu item description is required");
        }

        if (description.length() > 150) {
            throw new RuntimeException("Menu item description must be less than 150 characters");
        }
    }

    private void validatePrice(Double price) {
        if (price == null) {
            throw new RuntimeException("Menu item price is required");
        }

        if (price < 0) {
            throw new RuntimeException("Menu item price must be greater than or equal to 0");
        }

        if (price > 20000) {
            throw new RuntimeException("Menu item price must be less than or equal to 20000");
        }
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new RuntimeException("Restaurant is required for menu item");
        }

        if (restaurant.getIsAvailable() == null || !restaurant.getIsAvailable()) {
            throw new RuntimeException("Restaurant must be available to add menu item");
        }
    }
}

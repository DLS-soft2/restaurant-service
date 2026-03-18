package com.dls.restaurantservice.GraphQL;

import com.dls.restaurantservice.DTO.MenuItemRequest;
import com.dls.restaurantservice.DTO.MenuItemResponse;
import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Service.MenuItemService;
import com.dls.restaurantservice.Service.RestaurantService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RestaurantResolver {

    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    public RestaurantResolver(RestaurantService restaurantService, MenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    // ---- Queries ----

    @QueryMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.GetAllRestaurants();
    }

    @QueryMapping
    public RestaurantResponse getRestaurantById(@Argument String restaurantId) {
        return restaurantService.GetRestaurantById(restaurantId);
    }

    @QueryMapping
    public RestaurantResponse getRestaurantByName(@Argument String name) {
        return restaurantService.GetRestaurantByName(name);
    }

    @QueryMapping
    public List<RestaurantResponse> getRestaurantsByAvailability(@Argument Boolean isAvailable) {
        return restaurantService.GetRestaurantsByAvailability(isAvailable);
    }

    @QueryMapping
    public List<RestaurantResponse> getRestaurantsByOpenStatus(@Argument Boolean isOpen) {
        return restaurantService.GetRestaurantsByOpenStatus(isOpen);
    }

    @QueryMapping
    public List<MenuItemResponse> getMenuItemsByRestaurantId(@Argument String restaurantId) {
        return menuItemService.getMenuItemsByRestaurantId(restaurantId);
    }

    @QueryMapping
    public MenuItemResponse getMenuItemById(@Argument String menuItemId) {
        return menuItemService.getMenuItemById(menuItemId);
    }

    // ---- Mutations ----

    @MutationMapping
    public RestaurantResponse addRestaurant(
            @Argument String name,
            @Argument String address,
            @Argument String phoneNumber,
            @Argument String email,
            @Argument String description,
            @Argument String openingHours,
            @Argument Boolean isOpen,
            @Argument Boolean isAvailable) {

        RestaurantRequest request = new RestaurantRequest();
        request.setName(name);
        request.setAddress(address);
        request.setPhoneNumber(phoneNumber);
        request.setEmail(email);
        request.setDescription(description);
        request.setOpeningHours(openingHours);
        request.setIsOpen(isOpen);
        request.setIsAvailable(isAvailable);
        return restaurantService.AddRestaurant(request);
    }

    @MutationMapping
    public RestaurantResponse updateRestaurant(
            @Argument String restaurantId,
            @Argument String name,
            @Argument String address,
            @Argument String phoneNumber,
            @Argument String email,
            @Argument String description,
            @Argument String openingHours,
            @Argument Boolean isOpen,
            @Argument Boolean isAvailable) {

        RestaurantRequest request = new RestaurantRequest();
        request.setName(name);
        request.setAddress(address);
        request.setPhoneNumber(phoneNumber);
        request.setEmail(email);
        request.setDescription(description);
        request.setOpeningHours(openingHours);
        request.setIsOpen(isOpen);
        request.setIsAvailable(isAvailable);
        return restaurantService.UpdateRestaurant(restaurantId, request);
    }

    @MutationMapping
    public Boolean deleteRestaurant(@Argument String restaurantId) {
        restaurantService.DeleteRestaurant(restaurantId);
        return true;
    }

    @MutationMapping
    public MenuItemResponse addMenuItem(
            @Argument String name,
            @Argument String description,
            @Argument Double price,
            @Argument String restaurantId) {

        MenuItemRequest request = new MenuItemRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setRestaurantId(restaurantId);
        return menuItemService.addMenuItem(request);
    }

    @MutationMapping
    public MenuItemResponse updateMenuItem(
            @Argument String menuItemId,
            @Argument String name,
            @Argument String description,
            @Argument Double price,
            @Argument String restaurantId) {

        MenuItemRequest request = new MenuItemRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setRestaurantId(restaurantId);
        return menuItemService.updateMenuItem(menuItemId, request);
    }

    @MutationMapping
    public Boolean deleteMenuItem(@Argument String menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
        return true;
    }

    @SchemaMapping(typeName = "MenuItem", field = "restaurant")
    public RestaurantResponse restaurant(MenuItemResponse menuItem) {
        return restaurantService.GetRestaurantById(menuItem.getRestaurantId());
    }
}
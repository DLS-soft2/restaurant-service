package com.dls.restaurantservice.Controller;

import com.dls.restaurantservice.DTO.PageResponse;
import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;


    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }


    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.GetAllRestaurants();
    }

    @GetMapping("/available")
    public List<RestaurantResponse> getAvailableRestaurants() {
        return restaurantService.GetAvailableRestaurants();
    }

    @GetMapping("/name/{name}")
    public RestaurantResponse getRestaurantByName(@PathVariable String name) {
        return restaurantService.GetRestaurantByName(name);
    }

    @GetMapping("/available/{isAvailable}")
    public List<RestaurantResponse> getRestaurantsByAvailability(@PathVariable Boolean isAvailable) {
        return restaurantService.GetRestaurantsByAvailability(isAvailable);
    }

    @GetMapping("/open/{isOpen}")
    public List<RestaurantResponse> getRestaurantsByOpenStatus(@PathVariable Boolean isOpen) {
        return restaurantService.GetRestaurantsByOpenStatus(isOpen);
    }

    @GetMapping("/location/{location}")
    public List<RestaurantResponse> getRestaurantsByLocation(@PathVariable String location) {
        return restaurantService.GetRestaurantsByLocation(location);
    }


    @GetMapping("/{id}")
    public RestaurantResponse getRestaurantById(@PathVariable Long id) {
        return restaurantService.GetRestaurantById(id);
    }

    @PostMapping("/add")
    public RestaurantResponse addRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        return restaurantService.AddRestaurant(restaurantRequest);
    }

    @PutMapping("/update/{id}")
    public RestaurantResponse updateRestaurant(@PathVariable Long id, @RequestBody RestaurantRequest restaurantRequest) {
        return restaurantService.UpdateRestaurant(id, restaurantRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.DeleteRestaurant(id);
    }

    // ---- Pagination endpoints ----

    @GetMapping("/paged")
    public PageResponse<RestaurantResponse> getAllRestaurantsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.GetAllRestaurantsPaged(page, size);
    }

    @GetMapping("/paged/available/{isAvailable}")
    public PageResponse<RestaurantResponse> getAvailableRestaurantsPaged(
            @PathVariable Boolean isAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.GetAvailableRestaurantsPaged(isAvailable, page, size);
    }

    @GetMapping("/paged/open/{isOpen}")
    public PageResponse<RestaurantResponse> getOpenRestaurantsPaged(
            @PathVariable Boolean isOpen,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.GetOpenRestaurantsPaged(isOpen, page, size);
    }

    // ---- Søgning ----

    @GetMapping("/search")
    public PageResponse<RestaurantResponse> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.Search(query, page, size);
    }
}

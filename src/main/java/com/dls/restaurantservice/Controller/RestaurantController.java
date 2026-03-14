package com.dls.restaurantservice.Controller;

import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;


    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }


    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.GetAllRestaurants();
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
}

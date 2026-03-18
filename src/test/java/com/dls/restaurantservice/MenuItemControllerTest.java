package com.dls.restaurantservice;

import com.dls.restaurantservice.DTO.MenuItemRequest;
import com.dls.restaurantservice.DTO.MenuItemResponse;
import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Repository.MenuItemRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import com.dls.restaurantservice.Service.MenuItemService;
import com.dls.restaurantservice.Service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
public class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private RestaurantResponse createdRestaurant;
    private MenuItemRequest validMenuItemRequest;

    @BeforeEach
    void setUp() {
        // Rens databasen før hver test
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        RestaurantRequest restaurantRequest = new RestaurantRequest();
        restaurantRequest.setName("Test Restaurant");
        restaurantRequest.setAddress("Testgade 123");
        restaurantRequest.setPhoneNumber("12345678");
        restaurantRequest.setEmail("test@restaurant.dk");
        restaurantRequest.setDescription("En god testrestaurant med lækker mad");
        restaurantRequest.setOpeningHours("Man-Fre: 10:00-22:00");
        restaurantRequest.setIsOpen(true);
        restaurantRequest.setIsAvailable(true);
        createdRestaurant = restaurantService.AddRestaurant(restaurantRequest);

        validMenuItemRequest = new MenuItemRequest();
        validMenuItemRequest.setName("Margherita");
        validMenuItemRequest.setDescription("Klassisk pizza med tomatsauce og mozzarella.");
        validMenuItemRequest.setPrice(79.99);
        validMenuItemRequest.setRestaurantId(createdRestaurant.getRestaurantId());
    }

    @Test
    void getAllMenuItems_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/menu-item"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMenuItemById_existingId_returnsMenuItem() throws Exception {
        MenuItemResponse created = menuItemService.addMenuItem(validMenuItemRequest);

        mockMvc.perform(get("/api/v1/menu-item/" + created.getMenuItemId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Margherita"))
                .andExpect(jsonPath("$.price").value(79.99));
    }

    @Test
    void getMenuItemById_nonExistingId_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/menu-item/nonexistentid"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getMenuItemsByRestaurantId_existingRestaurant_returnsMenuItems() throws Exception {
        menuItemService.addMenuItem(validMenuItemRequest);

        mockMvc.perform(get("/api/v1/menu-item/restaurant/" + createdRestaurant.getRestaurantId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").value("Margherita"));
    }

    @Test
    void getMenuItemsByRestaurantId_nonExistingRestaurant_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/menu-item/restaurant/nonexistentid"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addMenuItem_validRequest_returnsCreatedMenuItem() throws Exception {
        mockMvc.perform(post("/api/v1/menu-item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMenuItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Margherita"))
                .andExpect(jsonPath("$.price").value(79.99))
                .andExpect(jsonPath("$.menuItemId").exists());
    }

    @Test
    void addMenuItem_missingName_returnsServerError() throws Exception {
        validMenuItemRequest.setName(null);

        mockMvc.perform(post("/api/v1/menu-item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMenuItemRequest)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void addMenuItem_negativePrice_returnsServerError() throws Exception {
        validMenuItemRequest.setPrice(-10.0);

        mockMvc.perform(post("/api/v1/menu-item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMenuItemRequest)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void addMenuItem_nonExistingRestaurant_returnsNotFound() throws Exception {
        validMenuItemRequest.setRestaurantId("nonexistentid"); // String i stedet for 999999L

        mockMvc.perform(post("/api/v1/menu-item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMenuItemRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateMenuItem_validRequest_returnsUpdatedMenuItem() throws Exception {
        MenuItemResponse created = menuItemService.addMenuItem(validMenuItemRequest);

        validMenuItemRequest.setName("Pepperoni");
        validMenuItemRequest.setPrice(89.99);

        mockMvc.perform(put("/api/v1/menu-item/update/" + created.getMenuItemId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMenuItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pepperoni"))
                .andExpect(jsonPath("$.price").value(89.99));
    }

    @Test
    void updateMenuItem_nonExistingId_returnsNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/menu-item/update/nonexistentid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMenuItemRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteMenuItem_existingId_returnsOk() throws Exception {
        MenuItemResponse created = menuItemService.addMenuItem(validMenuItemRequest);

        mockMvc.perform(delete("/api/v1/menu-item/delete/" + created.getMenuItemId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMenuItem_nonExistingId_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/menu-item/delete/nonexistentid"))
                .andExpect(status().is4xxClientError());
    }
}
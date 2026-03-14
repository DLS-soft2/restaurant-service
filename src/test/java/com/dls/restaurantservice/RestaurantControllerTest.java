package com.dls.restaurantservice;

import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    private RestaurantRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RestaurantRequest();
        validRequest.setName("Test Restaurant");
        validRequest.setAddress("Testgade 123");
        validRequest.setPhoneNumber("12345678");
        validRequest.setEmail("test@restaurant.dk");
        validRequest.setDescription("En god testrestaurant med lækker mad");
        validRequest.setOpeningHours("Man-Fre: 10:00-22:00");
        validRequest.setIsOpen(true);
        validRequest.setIsAvailable(true);
    }

    @Test
    void getAllRestaurants_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/restaurant"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getRestaurantById_existingId_returnsRestaurant() throws Exception {
        RestaurantResponse created = restaurantService.AddRestaurant(validRequest);

        mockMvc.perform(get("/api/v1/restaurant/" + created.getRestaurantId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.email").value("test@restaurant.dk"));
    }

    @Test
    void getRestaurantById_nonExistingId_returnsServerError() throws Exception {
        mockMvc.perform(get("/api/v1/restaurant/999999"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getRestaurantByName_existingName_returnsRestaurant() throws Exception {
        restaurantService.AddRestaurant(validRequest);

        mockMvc.perform(get("/api/v1/restaurant/name/Test Restaurant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }

    @Test
    void getRestaurantsByAvailability_returnsFilteredList() throws Exception {
        restaurantService.AddRestaurant(validRequest);

        mockMvc.perform(get("/api/v1/restaurant/available/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].isAvailable", everyItem(is(true))));
    }

    @Test
    void getRestaurantsByOpenStatus_returnsFilteredList() throws Exception {
        restaurantService.AddRestaurant(validRequest);

        mockMvc.perform(get("/api/v1/restaurant/open/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].isOpen", everyItem(is(true))));
    }

    @Test
    void getRestaurantsByLocation_returnsMatchingRestaurants() throws Exception {
        restaurantService.AddRestaurant(validRequest);

        mockMvc.perform(get("/api/v1/restaurant/location/Testgade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void addRestaurant_validRequest_returnsCreatedRestaurant() throws Exception {
        validRequest.setEmail("unique@restaurant.dk");

        mockMvc.perform(post("/api/v1/restaurant/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.restaurantId").exists());
    }

    @Test
    void addRestaurant_missingName_returnsServerError() throws Exception {
        validRequest.setName(null);

        mockMvc.perform(post("/api/v1/restaurant/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void addRestaurant_invalidEmail_returnsServerError() throws Exception {
        validRequest.setEmail("ikke-en-email");

        mockMvc.perform(post("/api/v1/restaurant/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void updateRestaurant_validRequest_returnsUpdatedRestaurant() throws Exception {
        RestaurantResponse created = restaurantService.AddRestaurant(validRequest);

        validRequest.setName("Opdateret Restaurant");
        validRequest.setEmail("opdateret@restaurant.dk");

        mockMvc.perform(put("/api/v1/restaurant/update/" + created.getRestaurantId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Opdateret Restaurant"));
    }

    @Test
    void updateRestaurant_nonExistingId_returnsServerError() throws Exception {
        mockMvc.perform(put("/api/v1/restaurant/update/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void deleteRestaurant_existingId_returnsOk() throws Exception {
        RestaurantResponse created = restaurantService.AddRestaurant(validRequest);

        mockMvc.perform(delete("/api/v1/restaurant/delete/" + created.getRestaurantId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRestaurant_nonExistingId_returnsServerError() throws Exception {
        mockMvc.perform(delete("/api/v1/restaurant/delete/999999"))
                .andExpect(status().is5xxServerError());
    }
}
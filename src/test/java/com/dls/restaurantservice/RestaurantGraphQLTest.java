package com.dls.restaurantservice;

import com.dls.restaurantservice.DTO.RestaurantRequest;
import com.dls.restaurantservice.DTO.RestaurantResponse;
import com.dls.restaurantservice.Service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureGraphQlTester
@ActiveProfiles("test")
@Transactional
@WithMockUser
public class RestaurantGraphQLTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private RestaurantService restaurantService;

    private RestaurantRequest validRequest;

    @BeforeEach
    void setUp() {

        validRequest = new RestaurantRequest();
        validRequest.setName("GraphQL Restaurant");
        validRequest.setAddress("GraphQL Gade 1");
        validRequest.setPhoneNumber("87654321");
        validRequest.setEmail("graphql@restaurant.dk");
        validRequest.setDescription("En restaurant til GraphQL tests med god mad");
        validRequest.setOpeningHours("Man-Fre: 10:00-22:00");
        validRequest.setIsOpen(true);
        validRequest.setIsAvailable(true);
    }

    @Test
    void getAllRestaurants_returnsOk() {
        graphQlTester.document("""
                query {
                    getAllRestaurants {
                        restaurantId
                        name
                        isOpen
                        isAvailable
                    }
                }
                """)
                .execute()
                .errors().verify()
                .path("getAllRestaurants").entityList(Object.class);
    }

    @Test
    void getRestaurantById_existingId_returnsRestaurant() {
        RestaurantResponse created = restaurantService.AddRestaurant(validRequest);

        graphQlTester.document("""
                query {
                    getRestaurantById(restaurantId: "%s") {
                        restaurantId
                        name
                        email
                    }
                }
                """.formatted(created.getRestaurantId()))
                .execute()
                .errors().verify()
                .path("getRestaurantById.name").entity(String.class).isEqualTo("GraphQL Restaurant")
                .path("getRestaurantById.email").entity(String.class).isEqualTo("graphql@restaurant.dk");
    }

    @Test
    void getRestaurantByName_existingName_returnsRestaurant() {
        restaurantService.AddRestaurant(validRequest);

        graphQlTester.document("""
                query {
                    getRestaurantByName(name: "GraphQL Restaurant") {
                        name
                        address
                    }
                }
                """)
                .execute()
                .errors().verify()
                .path("getRestaurantByName.name").entity(String.class).isEqualTo("GraphQL Restaurant");
    }

    @Test
    void getRestaurantsByAvailability_returnsFilteredList() {
        restaurantService.AddRestaurant(validRequest);

        graphQlTester.document("""
                query {
                    getRestaurantsByAvailability(isAvailable: true) {
                        name
                        isAvailable
                    }
                }
                """)
                .execute()
                .errors().verify()
                .path("getRestaurantsByAvailability").entityList(Object.class);
    }

    @Test
    void addRestaurant_validInput_returnsCreatedRestaurant() {
        graphQlTester.document("""
                mutation {
                    addRestaurant(
                        name: "Ny Restaurant"
                        address: "Ny Gade 42"
                        phoneNumber: "11223344"
                        email: "ny@restaurant.dk"
                        description: "En brandny restaurant med lækker mad"
                        openingHours: "Man-Fre: 11:00-21:00"
                        isOpen: true
                        isAvailable: true
                    ) {
                        restaurantId
                        name
                        email
                    }
                }
                """)
                .execute()
                .errors().verify()
                .path("addRestaurant.name").entity(String.class).isEqualTo("Ny Restaurant")
                .path("addRestaurant.restaurantId").hasValue();
    }

    @Test
    void updateRestaurant_validInput_returnsUpdatedRestaurant() {
        RestaurantResponse created = restaurantService.AddRestaurant(validRequest);

        graphQlTester.document("""
                mutation {
                    updateRestaurant(
                        restaurantId: "%s"
                        name: "Opdateret Restaurant"
                        address: "GraphQL Gade 1"
                        phoneNumber: "87654321"
                        email: "opdateret@restaurant.dk"
                        description: "En opdateret restaurant med endnu bedre mad"
                        openingHours: "Man-Fre: 10:00-22:00"
                        isOpen: true
                        isAvailable: true
                    ) {
                        name
                        email
                    }
                }
                """.formatted(created.getRestaurantId()))
                .execute()
                .errors().verify()
                .path("updateRestaurant.name").entity(String.class).isEqualTo("Opdateret Restaurant");
    }

    @Test
    void deleteRestaurant_existingId_returnsTrue() {
        RestaurantResponse created = restaurantService.AddRestaurant(validRequest);

        graphQlTester.document("""
                mutation {
                    deleteRestaurant(restaurantId: "%s")
                }
                """.formatted(created.getRestaurantId()))
                .execute()
                .errors().verify()
                .path("deleteRestaurant").entity(Boolean.class).isEqualTo(true);
    }
}
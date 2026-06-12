package com.dls.restaurantservice;

import com.dls.restaurantservice.DTO.PendingOrderResponse;
import com.dls.restaurantservice.Document.PendingOrder;
import com.dls.restaurantservice.Document.Restaurant;
import com.dls.restaurantservice.Kafka.RestaurantAcceptedEvent;
import com.dls.restaurantservice.Kafka.RestaurantRejectedEvent;
import com.dls.restaurantservice.Repository.PendingOrderRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import com.dls.restaurantservice.Service.OrderManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderManagementServiceTest {

    @Mock
    private PendingOrderRepository pendingOrderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private OrderManagementService service;

    private Restaurant restaurant;
    private PendingOrder pendingOrder;

    @BeforeEach
    void setUp() {
        service = new OrderManagementService(
                pendingOrderRepository, restaurantRepository, kafkaTemplate, "restaurants");

        restaurant = new Restaurant();
        restaurant.setRestaurantId("rest-789");
        restaurant.setKeycloakId("kc-user-1");
        restaurant.setEstimatedPrepTimeMinutes(20);
        restaurant.setAddress("Restaurant Street 1");

        pendingOrder = new PendingOrder("order-123", "cust-456", "rest-789", "pay-111", 99.99, "123 Main St");
        pendingOrder.setId("mongo-id-1");
    }

    @Test
    void acceptOrder_happyPath() {
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-123")).thenReturn(Optional.of(pendingOrder));

        PendingOrderResponse response = service.acceptOrder("kc-user-1", "order-123");

        assertEquals("ACCEPTED", response.getStatus());
        assertEquals("order-123", response.getOrderId());

        ArgumentCaptor<PendingOrder> orderCaptor = ArgumentCaptor.forClass(PendingOrder.class);
        verify(pendingOrderRepository).save(orderCaptor.capture());
        assertEquals("ACCEPTED", orderCaptor.getValue().getStatus());

        ArgumentCaptor<RestaurantAcceptedEvent> eventCaptor = ArgumentCaptor.forClass(RestaurantAcceptedEvent.class);
        verify(kafkaTemplate).send(eq("restaurants"), eq("order-123"), eventCaptor.capture());

        RestaurantAcceptedEvent published = eventCaptor.getValue();
        assertEquals("RestaurantAccepted", published.getEventType());
        assertEquals("order-123", published.getOrderId());
        assertEquals("cust-456", published.getCustomerId());
        assertEquals("rest-789", published.getRestaurantId());
        assertEquals(20, published.getEstimatedPrepTime());
    }

    @Test
    void rejectOrder_happyPath() {
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-123")).thenReturn(Optional.of(pendingOrder));

        PendingOrderResponse response = service.rejectOrder("kc-user-1", "order-123", "Too busy");

        assertEquals("REJECTED", response.getStatus());

        ArgumentCaptor<PendingOrder> orderCaptor = ArgumentCaptor.forClass(PendingOrder.class);
        verify(pendingOrderRepository).save(orderCaptor.capture());
        assertEquals("REJECTED", orderCaptor.getValue().getStatus());

        ArgumentCaptor<RestaurantRejectedEvent> eventCaptor = ArgumentCaptor.forClass(RestaurantRejectedEvent.class);
        verify(kafkaTemplate).send(eq("restaurants"), eq("order-123"), eventCaptor.capture());

        RestaurantRejectedEvent published = eventCaptor.getValue();
        assertEquals("RestaurantRejected", published.getEventType());
        assertEquals("order-123", published.getOrderId());
        assertEquals("cust-456", published.getCustomerId());
        assertEquals("rest-789", published.getRestaurantId());
        assertEquals("Too busy", published.getReason());
    }

    @Test
    void acceptOrder_alreadyDecided_returns409() {
        pendingOrder.setStatus("ACCEPTED");
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-123")).thenReturn(Optional.of(pendingOrder));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.acceptOrder("kc-user-1", "order-123"));
        assertEquals(409, ex.getStatusCode().value());

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    void rejectOrder_alreadyDecided_returns409() {
        pendingOrder.setStatus("REJECTED");
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-123")).thenReturn(Optional.of(pendingOrder));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.rejectOrder("kc-user-1", "order-123", "Too busy"));
        assertEquals(409, ex.getStatusCode().value());

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    void acceptOrder_wrongRestaurant_returns403() {
        pendingOrder.setRestaurantId("other-rest");
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-123")).thenReturn(Optional.of(pendingOrder));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.acceptOrder("kc-user-1", "order-123"));
        assertEquals(403, ex.getStatusCode().value());

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    void acceptOrder_noRestaurantForUser_returns404() {
        when(restaurantRepository.findByKeycloakId("kc-unknown")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.acceptOrder("kc-unknown", "order-123"));
        assertEquals(404, ex.getStatusCode().value());

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    void acceptOrder_orderNotFound_returns404() {
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-999")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.acceptOrder("kc-user-1", "order-999"));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void acceptOrder_idempotent_secondCallReturns409() {
        // First call succeeds
        when(restaurantRepository.findByKeycloakId("kc-user-1")).thenReturn(Optional.of(restaurant));
        when(pendingOrderRepository.findByOrderId("order-123")).thenReturn(Optional.of(pendingOrder));

        service.acceptOrder("kc-user-1", "order-123");

        // After first call, status is ACCEPTED
        pendingOrder.setStatus("ACCEPTED");

        // Second call returns 409
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.acceptOrder("kc-user-1", "order-123"));
        assertEquals(409, ex.getStatusCode().value());
    }
}

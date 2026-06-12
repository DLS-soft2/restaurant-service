package com.dls.restaurantservice.Service;

import com.dls.restaurantservice.DTO.PendingOrderResponse;
import com.dls.restaurantservice.Document.PendingOrder;
import com.dls.restaurantservice.Document.Restaurant;
import com.dls.restaurantservice.Kafka.RestaurantAcceptedEvent;
import com.dls.restaurantservice.Kafka.RestaurantRejectedEvent;
import com.dls.restaurantservice.Repository.PendingOrderRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderManagementService {

    private final PendingOrderRepository pendingOrderRepository;
    private final RestaurantRepository restaurantRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String restaurantsTopic;

    public OrderManagementService(
            PendingOrderRepository pendingOrderRepository,
            RestaurantRepository restaurantRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topic.restaurants}") String restaurantsTopic) {
        this.pendingOrderRepository = pendingOrderRepository;
        this.restaurantRepository = restaurantRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.restaurantsTopic = restaurantsTopic;
    }

    public List<PendingOrderResponse> getPendingOrders(String keycloakId) {
        Restaurant restaurant = resolveRestaurant(keycloakId);
        return pendingOrderRepository.findByRestaurantIdAndStatus(restaurant.getRestaurantId(), "PENDING")
                .stream()
                .map(PendingOrderResponse::new)
                .collect(Collectors.toList());
    }

    public PendingOrderResponse acceptOrder(String keycloakId, String orderId) {
        Restaurant restaurant = resolveRestaurant(keycloakId);
        PendingOrder order = loadAndValidate(orderId, restaurant.getRestaurantId());

        order.setStatus("ACCEPTED");
        pendingOrderRepository.save(order);

        Integer prepTime = restaurant.getEstimatedPrepTimeMinutes() != null
                ? restaurant.getEstimatedPrepTimeMinutes() : 15;

        RestaurantAcceptedEvent event = new RestaurantAcceptedEvent(
                order.getOrderId(), order.getCustomerId(), restaurant.getRestaurantId(),
                prepTime, order.getDeliveryAddress(), restaurant.getAddress());

        kafkaTemplate.send(restaurantsTopic, order.getOrderId(), event);
        return new PendingOrderResponse(order);
    }

    public PendingOrderResponse rejectOrder(String keycloakId, String orderId, String reason) {
        Restaurant restaurant = resolveRestaurant(keycloakId);
        PendingOrder order = loadAndValidate(orderId, restaurant.getRestaurantId());

        order.setStatus("REJECTED");
        pendingOrderRepository.save(order);

        RestaurantRejectedEvent event = new RestaurantRejectedEvent(
                order.getOrderId(), order.getCustomerId(), restaurant.getRestaurantId(), reason);

        kafkaTemplate.send(restaurantsTopic, order.getOrderId(), event);
        return new PendingOrderResponse(order);
    }

    private Restaurant resolveRestaurant(String keycloakId) {
        return restaurantRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No restaurant found for current user"));
    }

    private PendingOrder loadAndValidate(String orderId, String callerRestaurantId) {
        PendingOrder order = pendingOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pending order not found: " + orderId));

        if (!order.getRestaurantId().equals(callerRestaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order belongs to a different restaurant");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already decided: " + order.getStatus());
        }

        return order;
    }
}

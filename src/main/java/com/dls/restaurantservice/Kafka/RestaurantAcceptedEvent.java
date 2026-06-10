package com.dls.restaurantservice.Kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class RestaurantAcceptedEvent {

    @JsonProperty("event_type")
    private String eventType = "RestaurantAccepted";

    @JsonProperty("event_id")
    private String eventId = UUID.randomUUID().toString();

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("restaurant_id")
    private String restaurantId;

    @JsonProperty("estimated_prep_time")
    private Integer estimatedPrepTime;

    @JsonProperty("timestamp")
    private String timestamp = Instant.now().toString();

    public RestaurantAcceptedEvent() {}

    public RestaurantAcceptedEvent(String orderId, String customerId, String restaurantId, Integer estimatedPrepTime) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.estimatedPrepTime = estimatedPrepTime;
    }
}

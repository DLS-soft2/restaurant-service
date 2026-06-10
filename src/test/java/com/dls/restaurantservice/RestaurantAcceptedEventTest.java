package com.dls.restaurantservice;

import com.dls.restaurantservice.Kafka.RestaurantAcceptedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantAcceptedEventTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serialization_usesSnakeCaseFieldNames() throws Exception {
        RestaurantAcceptedEvent event = new RestaurantAcceptedEvent("order-1", "cust-2", "rest-1", 20);

        String json = objectMapper.writeValueAsString(event);

        assertTrue(json.contains("\"event_type\""), "Missing event_type in: " + json);
        assertTrue(json.contains("\"event_id\""), "Missing event_id in: " + json);
        assertTrue(json.contains("\"order_id\""), "Missing order_id in: " + json);
        assertTrue(json.contains("\"customer_id\""), "Missing customer_id in: " + json);
        assertTrue(json.contains("\"restaurant_id\""), "Missing restaurant_id in: " + json);
        assertTrue(json.contains("\"estimated_prep_time\""), "Missing estimated_prep_time in: " + json);
        assertTrue(json.contains("\"timestamp\""), "Missing timestamp in: " + json);

        assertFalse(json.contains("\"eventType\""), "camelCase eventType found in: " + json);
        assertFalse(json.contains("\"eventId\""), "camelCase eventId found in: " + json);
        assertFalse(json.contains("\"orderId\""), "camelCase orderId found in: " + json);
        assertFalse(json.contains("\"customerId\""), "camelCase customerId found in: " + json);
        assertFalse(json.contains("\"restaurantId\""), "camelCase restaurantId found in: " + json);
        assertFalse(json.contains("\"estimatedPrepTime\""), "camelCase estimatedPrepTime found in: " + json);
    }

    @Test
    void defaultValues_arePopulated() {
        RestaurantAcceptedEvent event = new RestaurantAcceptedEvent("order-1", "cust-2", "rest-1", 15);

        assertEquals("RestaurantAccepted", event.getEventType());
        assertNotNull(event.getEventId());
        assertFalse(event.getEventId().isEmpty());
        assertNotNull(event.getTimestamp());
        assertEquals("order-1", event.getOrderId());
        assertEquals("cust-2", event.getCustomerId());
        assertEquals("rest-1", event.getRestaurantId());
        assertEquals(15, event.getEstimatedPrepTime());
    }
}

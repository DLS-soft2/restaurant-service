package com.dls.restaurantservice.Kafka;

import com.dls.restaurantservice.Document.ProcessedEvent;
import com.dls.restaurantservice.Document.Restaurant;
import com.dls.restaurantservice.Repository.ProcessedEventRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final RestaurantRepository restaurantRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String restaurantsTopic;

    public PaymentEventConsumer(
            RestaurantRepository restaurantRepository,
            ProcessedEventRepository processedEventRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topic.restaurants}") String restaurantsTopic) {
        this.restaurantRepository = restaurantRepository;
        this.processedEventRepository = processedEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.restaurantsTopic = restaurantsTopic;
    }

    @KafkaListener(topics = "${app.kafka.topic.payments}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        JsonNode node;
        try {
            node = objectMapper.readTree(message);
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse Kafka message: {}", e.getMessage());
            return;
        }

        String eventType = node.has("event_type") ? node.get("event_type").asText() : null;
        if (!"PaymentAuthorized".equals(eventType)) {
            log.info("Ignoring event with type: {}", eventType);
            return;
        }

        String eventId = node.has("event_id") ? node.get("event_id").asText() : null;
        if (eventId == null) {
            log.warn("PaymentAuthorized event missing event_id, skipping");
            return;
        }

        if (processedEventRepository.existsById(eventId)) {
            log.info("Duplicate event_id={}, skipping", eventId);
            return;
        }

        PaymentAuthorizedEvent event;
        try {
            event = objectMapper.treeToValue(node, PaymentAuthorizedEvent.class);
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize PaymentAuthorizedEvent: {}", e.getMessage());
            return;
        }

        String restaurantId = event.getRestaurantId();
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(
                restaurantId != null ? restaurantId : "");

        if (restaurantOpt.isEmpty()) {
            log.warn("Restaurant not found for id={}, marking event as processed", restaurantId);
            processedEventRepository.save(new ProcessedEvent(eventId, Instant.now()));
            return;
        }

        Restaurant restaurant = restaurantOpt.get();
        Integer prepTime = restaurant.getEstimatedPrepTimeMinutes() != null
                ? restaurant.getEstimatedPrepTimeMinutes() : 15;

        RestaurantAcceptedEvent accepted = new RestaurantAcceptedEvent(
                event.getOrderId(), event.getCustomerId(), restaurantId, prepTime,
                event.getDeliveryAddress(), restaurant.getAddress());

        kafkaTemplate.send(restaurantsTopic, event.getOrderId(), accepted);
        log.info("Published RestaurantAccepted for order_id={}", event.getOrderId());

        processedEventRepository.save(new ProcessedEvent(eventId, Instant.now()));
        log.info("Marked event_id={} as processed", eventId);
    }
}

package com.dls.restaurantservice.Kafka;

import com.dls.restaurantservice.Document.PendingOrder;
import com.dls.restaurantservice.Document.ProcessedEvent;
import com.dls.restaurantservice.Repository.PendingOrderRepository;
import com.dls.restaurantservice.Repository.ProcessedEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final PendingOrderRepository pendingOrderRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;

    public PaymentEventConsumer(
            PendingOrderRepository pendingOrderRepository,
            ProcessedEventRepository processedEventRepository,
            ObjectMapper objectMapper) {
        this.pendingOrderRepository = pendingOrderRepository;
        this.processedEventRepository = processedEventRepository;
        this.objectMapper = objectMapper;
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

        // Idempotency: skip if a PendingOrder already exists for this orderId
        if (pendingOrderRepository.existsByOrderId(event.getOrderId())) {
            log.info("PendingOrder already exists for order_id={}, skipping", event.getOrderId());
            processedEventRepository.save(new ProcessedEvent(eventId, Instant.now()));
            return;
        }

        PendingOrder pendingOrder = new PendingOrder(
                event.getOrderId(), event.getCustomerId(), event.getRestaurantId(),
                event.getPaymentId(), event.getAmount(), event.getDeliveryAddress());

        pendingOrderRepository.save(pendingOrder);
        log.info("Saved PendingOrder for order_id={}", event.getOrderId());

        processedEventRepository.save(new ProcessedEvent(eventId, Instant.now()));
        log.info("Marked event_id={} as processed", eventId);
    }
}

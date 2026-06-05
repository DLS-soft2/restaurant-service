package com.dls.restaurantservice;

import com.dls.restaurantservice.Document.ProcessedEvent;
import com.dls.restaurantservice.Document.Restaurant;
import com.dls.restaurantservice.Kafka.PaymentEventConsumer;
import com.dls.restaurantservice.Kafka.RestaurantAcceptedEvent;
import com.dls.restaurantservice.Repository.ProcessedEventRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventConsumerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ProcessedEventRepository processedEventRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private PaymentEventConsumer consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        consumer = new PaymentEventConsumer(
                restaurantRepository,
                processedEventRepository,
                kafkaTemplate,
                objectMapper,
                "restaurants"
        );
    }

    @Test
    void happyPath_publishesRestaurantAcceptedAndSavesProcessedEvent() {
        String message = """
                {
                    "event_type": "PaymentAuthorized",
                    "event_id": "evt-001",
                    "order_id": "order-123",
                    "customer_id": "cust-456",
                    "restaurant_id": "rest-789",
                    "payment_id": "pay-111",
                    "amount": 99.99,
                    "timestamp": "2026-06-05T10:00:00Z"
                }
                """;

        when(processedEventRepository.existsById("evt-001")).thenReturn(false);

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("rest-789");
        restaurant.setEstimatedPrepTimeMinutes(20);
        when(restaurantRepository.findById("rest-789")).thenReturn(Optional.of(restaurant));

        consumer.consume(message);

        ArgumentCaptor<RestaurantAcceptedEvent> eventCaptor = ArgumentCaptor.forClass(RestaurantAcceptedEvent.class);
        verify(kafkaTemplate).send(eq("restaurants"), eq("order-123"), eventCaptor.capture());

        RestaurantAcceptedEvent published = eventCaptor.getValue();
        assertEquals("RestaurantAccepted", published.getEventType());
        assertEquals("order-123", published.getOrderId());
        assertEquals("cust-456", published.getCustomerId());
        assertEquals(20, published.getEstimatedPrepTime());
        assertNotNull(published.getEventId());
        assertNotNull(published.getTimestamp());

        ArgumentCaptor<ProcessedEvent> processedCaptor = ArgumentCaptor.forClass(ProcessedEvent.class);
        verify(processedEventRepository).save(processedCaptor.capture());
        assertEquals("evt-001", processedCaptor.getValue().getEventId());
    }

    @Test
    void duplicateEventId_skipsProcessingEntirely() {
        String message = """
                {
                    "event_type": "PaymentAuthorized",
                    "event_id": "evt-dup",
                    "order_id": "order-123",
                    "customer_id": "cust-456",
                    "restaurant_id": "rest-789",
                    "payment_id": "pay-111",
                    "amount": 50.0,
                    "timestamp": "2026-06-05T10:00:00Z"
                }
                """;

        when(processedEventRepository.existsById("evt-dup")).thenReturn(true);

        consumer.consume(message);

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
        verify(processedEventRepository, never()).save(any());
    }

    @Test
    void restaurantNotFound_marksProcessedButDoesNotPublish() {
        String message = """
                {
                    "event_type": "PaymentAuthorized",
                    "event_id": "evt-003",
                    "order_id": "order-999",
                    "customer_id": "cust-456",
                    "restaurant_id": "nonexistent",
                    "payment_id": "pay-222",
                    "amount": 25.0,
                    "timestamp": "2026-06-05T10:00:00Z"
                }
                """;

        when(processedEventRepository.existsById("evt-003")).thenReturn(false);
        when(restaurantRepository.findById("nonexistent")).thenReturn(Optional.empty());

        consumer.consume(message);

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());

        ArgumentCaptor<ProcessedEvent> captor = ArgumentCaptor.forClass(ProcessedEvent.class);
        verify(processedEventRepository).save(captor.capture());
        assertEquals("evt-003", captor.getValue().getEventId());
    }

    @Test
    void nonPaymentAuthorizedEvent_ignoredCompletely() {
        String message = """
                {
                    "event_type": "PaymentFailed",
                    "event_id": "evt-004",
                    "order_id": "order-555",
                    "customer_id": "cust-789",
                    "reason": "Insufficient funds",
                    "timestamp": "2026-06-05T10:00:00Z"
                }
                """;

        consumer.consume(message);

        verify(processedEventRepository, never()).existsById(anyString());
        verify(processedEventRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
        verify(restaurantRepository, never()).findById(anyString());
    }
}

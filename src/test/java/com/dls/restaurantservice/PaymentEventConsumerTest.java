package com.dls.restaurantservice;

import com.dls.restaurantservice.Document.PendingOrder;
import com.dls.restaurantservice.Document.ProcessedEvent;
import com.dls.restaurantservice.Kafka.PaymentEventConsumer;
import com.dls.restaurantservice.Repository.PendingOrderRepository;
import com.dls.restaurantservice.Repository.ProcessedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventConsumerTest {

    @Mock
    private PendingOrderRepository pendingOrderRepository;

    @Mock
    private ProcessedEventRepository processedEventRepository;

    private PaymentEventConsumer consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        consumer = new PaymentEventConsumer(
                pendingOrderRepository,
                processedEventRepository,
                objectMapper
        );
    }

    @Test
    void happyPath_savesPendingOrderWithStatusPending() {
        String message = """
                {
                    "event_type": "PaymentAuthorized",
                    "event_id": "evt-001",
                    "order_id": "order-123",
                    "customer_id": "cust-456",
                    "restaurant_id": "rest-789",
                    "payment_id": "pay-111",
                    "amount": 99.99,
                    "delivery_address": "123 Main St",
                    "timestamp": "2026-06-05T10:00:00Z"
                }
                """;

        when(processedEventRepository.existsById("evt-001")).thenReturn(false);
        when(pendingOrderRepository.existsByOrderId("order-123")).thenReturn(false);

        consumer.consume(message);

        ArgumentCaptor<PendingOrder> orderCaptor = ArgumentCaptor.forClass(PendingOrder.class);
        verify(pendingOrderRepository).save(orderCaptor.capture());

        PendingOrder saved = orderCaptor.getValue();
        assertEquals("order-123", saved.getOrderId());
        assertEquals("cust-456", saved.getCustomerId());
        assertEquals("rest-789", saved.getRestaurantId());
        assertEquals("pay-111", saved.getPaymentId());
        assertEquals(99.99, saved.getAmount());
        assertEquals("PENDING", saved.getStatus());
        assertNotNull(saved.getCreatedAt());

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

        verify(pendingOrderRepository, never()).save(any());
        verify(processedEventRepository, never()).save(any());
    }

    @Test
    void duplicateOrderId_skipsCreationButMarksProcessed() {
        String message = """
                {
                    "event_type": "PaymentAuthorized",
                    "event_id": "evt-002",
                    "order_id": "order-123",
                    "customer_id": "cust-456",
                    "restaurant_id": "rest-789",
                    "payment_id": "pay-111",
                    "amount": 50.0,
                    "timestamp": "2026-06-05T10:00:00Z"
                }
                """;

        when(processedEventRepository.existsById("evt-002")).thenReturn(false);
        when(pendingOrderRepository.existsByOrderId("order-123")).thenReturn(true);

        consumer.consume(message);

        verify(pendingOrderRepository, never()).save(any());
        verify(processedEventRepository).save(any());
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
        verify(pendingOrderRepository, never()).save(any());
    }
}

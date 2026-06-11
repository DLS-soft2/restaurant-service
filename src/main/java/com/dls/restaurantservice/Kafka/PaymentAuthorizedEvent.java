package com.dls.restaurantservice.Kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentAuthorizedEvent {

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("restaurant_id")
    private String restaurantId;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("delivery_address")
    private String deliveryAddress;

    @JsonProperty("timestamp")
    private String timestamp;
}

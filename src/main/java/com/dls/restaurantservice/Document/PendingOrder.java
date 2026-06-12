package com.dls.restaurantservice.Document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "pending_orders")
public class PendingOrder {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderId;

    private String customerId;
    private String restaurantId;
    private String paymentId;
    private Double amount;
    private String status;
    private String deliveryAddress;
    private Instant createdAt;

    public PendingOrder() {}

    public PendingOrder(String orderId, String customerId, String restaurantId,
                        String paymentId, Double amount, String deliveryAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.deliveryAddress = deliveryAddress;
        this.status = "PENDING";
        this.createdAt = Instant.now();
    }
}

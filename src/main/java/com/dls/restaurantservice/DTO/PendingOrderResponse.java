package com.dls.restaurantservice.DTO;

import com.dls.restaurantservice.Document.PendingOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PendingOrderResponse {
    private String orderId;
    private String customerId;
    private String restaurantId;
    private String paymentId;
    private Double amount;
    private String status;
    private String createdAt;

    public PendingOrderResponse(PendingOrder order) {
        this.orderId = order.getOrderId();
        this.customerId = order.getCustomerId();
        this.restaurantId = order.getRestaurantId();
        this.paymentId = order.getPaymentId();
        this.amount = order.getAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt() != null ? order.getCreatedAt().toString() : null;
    }
}

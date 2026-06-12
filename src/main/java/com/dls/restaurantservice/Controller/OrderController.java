package com.dls.restaurantservice.Controller;

import com.dls.authlib.Permission;
import com.dls.authlib.RequirePermission;
import com.dls.restaurantservice.DTO.OrderActionRequest;
import com.dls.restaurantservice.DTO.PendingOrderResponse;
import com.dls.restaurantservice.Service.OrderManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/restaurants/orders")
public class OrderController {

    private final OrderManagementService orderManagementService;

    public OrderController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/pending")
    @RequirePermission(Permission.ORDERS_UPDATE)
    public List<PendingOrderResponse> getPendingOrders(HttpServletRequest request) {
        String keycloakId = request.getHeader("X-User-Id");
        return orderManagementService.getPendingOrders(keycloakId);
    }

    @PostMapping("/{orderId}/accept")
    @RequirePermission(Permission.ORDERS_UPDATE)
    public ResponseEntity<PendingOrderResponse> acceptOrder(
            @PathVariable String orderId, HttpServletRequest request) {
        String keycloakId = request.getHeader("X-User-Id");
        return ResponseEntity.ok(orderManagementService.acceptOrder(keycloakId, orderId));
    }

    @PostMapping("/{orderId}/reject")
    @RequirePermission(Permission.ORDERS_UPDATE)
    public ResponseEntity<PendingOrderResponse> rejectOrder(
            @PathVariable String orderId,
            @RequestBody OrderActionRequest body,
            HttpServletRequest request) {
        String keycloakId = request.getHeader("X-User-Id");
        return ResponseEntity.ok(orderManagementService.rejectOrder(keycloakId, orderId, body.getReason()));
    }
}

package com.moo.orders.dto.response;

import java.util.UUID;
import com.moo.orders.entity.Order;

public record OrderResponse(UUID orderId, String customerId, String productId, int quantity, String status, String createdAt) {
    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getOrderId(), order.getCustomerId(), order.getProductId(), order.getQuantity(),
                order.getStatus(), order.getCreatedAt().toString());
    }
}

package com.moo.orders.service;

import java.util.Objects;
import java.util.UUID;
import com.moo.orders.client.InventoryClient;
import com.moo.orders.dto.request.OrderRequest;
import com.moo.orders.dto.response.InventoryResponse;
import com.moo.orders.dto.response.OrderResponse;
import com.moo.orders.dto.response.ReservationResponse;
import com.moo.orders.entity.Order;
import com.moo.orders.exception.OrderNotFoundException;
import com.moo.orders.exception.ProductNotFoundException;
import com.moo.orders.exception.StockNotReservedException;
import com.moo.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderRequest request) {
        // Before creating the order lets validate the request:
        if (request.customerId() == null || request.customerId().isBlank()) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (request.productId() == null || request.productId().isBlank()) {
            throw new IllegalArgumentException("Product ID is required");
        }

        if (request.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // 1. Check the product exists in the Inventory Service
        InventoryResponse product = inventoryClient.getProduct(request.productId());
        if (Objects.isNull(product)) { // This is an extra check to ensure if the product exist or not
            throw new ProductNotFoundException("Product '" + request.productId() + "' not found");
        }

        // 2. Attempt to reserve the requested quantity
        ReservationResponse reserve = inventoryClient.reserve(request.productId(), request.quantity());
        if (!reserve.reserved()) {
            throw new StockNotReservedException(reserve.reason());
        }

        //3. Create record into the order table
        Order order = new Order(request.customerId(), request.productId(), request.quantity());
        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        if (Objects.isNull(order)) {
            throw new OrderNotFoundException("Order '" + orderId + "' not found");
        }

        return OrderResponse.of(order);
    }
}

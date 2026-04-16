package com.moo.orders.repository;

import com.moo.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(UUID orderId);
}

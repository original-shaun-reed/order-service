package com.moo.orders.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders") // Using "orders" as the table name because ORDER is a reserved keyword in SQL (used in ORDER BY).
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID orderId;

    private String customerId;

    private String productId;

    private int quantity;

    private String status;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.orderId = UUID.randomUUID();
        this.status = "CONFIRMED"; // This should be in an enum. (Not going to assume that there are other statuses)
        this.createdAt = Instant.now();
    }

    public Order(String customerId, String productId, int quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }
}


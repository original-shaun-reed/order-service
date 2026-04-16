package com.moo.orders.dto.request;

public record OrderRequest (String productId, int quantity, String customerId) {}

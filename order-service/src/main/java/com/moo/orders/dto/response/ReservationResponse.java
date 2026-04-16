package com.moo.orders.dto.response;


public record ReservationResponse (boolean reserved, String reason, int remaining) {}

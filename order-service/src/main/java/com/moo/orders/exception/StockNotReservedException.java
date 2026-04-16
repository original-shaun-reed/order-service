package com.moo.orders.exception;

public class StockNotReservedException extends RuntimeException {
    public StockNotReservedException(String message) {
        super(message);
    }
}

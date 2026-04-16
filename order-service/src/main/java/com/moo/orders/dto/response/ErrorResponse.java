package com.moo.orders.dto.response;

import java.io.Serializable;

public record ErrorResponse(String message) implements Serializable {
}

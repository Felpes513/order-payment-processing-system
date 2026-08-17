package com.felipe.orderservice.shared.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}
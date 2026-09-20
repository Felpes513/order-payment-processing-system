package com.inventoryservice.inventory_service.shared.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}

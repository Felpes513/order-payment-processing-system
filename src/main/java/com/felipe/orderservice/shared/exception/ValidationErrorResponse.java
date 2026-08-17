package com.felipe.orderservice.shared.exception;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldErrorResponse> fieldErrors
) {
}
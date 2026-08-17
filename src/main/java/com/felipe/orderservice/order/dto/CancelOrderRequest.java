package com.felipe.orderservice.order.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelOrderRequest(

        @NotBlank(message = "Reason is required")
        String reason
) {
}

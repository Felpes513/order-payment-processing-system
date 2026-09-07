package com.felipe.orderservice.order.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelOrderRequest(

        @NotBlank(message = "Reason is required")
        /*@ spec_public @*/ String reason
) {
    //@ public invariant reason != null && !reason.isBlank();
}

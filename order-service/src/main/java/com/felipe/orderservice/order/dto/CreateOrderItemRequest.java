package com.felipe.orderservice.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemRequest(

        @NotNull(message = "Product id is required")
        /*@ spec_public @*/ UUID productId,

        @NotBlank(message = "Product name is required")
        /*@ spec_public @*/ String productName,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.00", inclusive = true, message = "Unit price cannot be negative")
        /*@ spec_public @*/ BigDecimal unitPrice,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than zero")
        /*@ spec_public @*/ Integer quantity
){
    //@ public invariant productId != null;
    //@ public invariant productName != null && !productName.isBlank();
    //@ public invariant unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
    //@ public invariant quantity != null && quantity > 0;
}

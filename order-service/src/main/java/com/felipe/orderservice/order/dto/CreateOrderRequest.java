package com.felipe.orderservice.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull(message = "Customer id is required")
        /*@ spec_public @*/ UUID customerId,

        @NotEmpty(message = "Order must have at least one item")
        /*@ spec_public @*/ List<@Valid CreateOrderItemRequest> items
){
    //@ public invariant customerId != null;
    //@ public invariant items != null && !items.isEmpty();
}

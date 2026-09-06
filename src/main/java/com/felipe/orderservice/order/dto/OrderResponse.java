package com.felipe.orderservice.order.dto;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        /*@ spec_public @*/ UUID id,
        /*@ spec_public @*/ UUID customerId,
        /*@ spec_public @*/ OrderStatus status,
        /*@ spec_public @*/ BigDecimal totalAmount,
        /*@ spec_public @*/ List<OrderItemResponse> items,
        /*@ spec_public @*/ Instant createdAt,
        /*@ spec_public @*/ Instant updatedAt
) {

    //@ public invariant id != null;
    //@ public invariant customerId != null;
    //@ public invariant status != null;
    //@ public invariant totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) >= 0;
    //@ public invariant items != null;
    //@ public invariant createdAt != null;
    //@ public invariant updatedAt != null;

    /*@
      @ public normal_behavior
      @   requires order != null;
      @   ensures \result != null;
      @   ensures \result.id == order.getId();
      @   ensures \result.customerId == order.getCustomerId();
      @   ensures \result.status == order.getStatus();
      @   ensures \result.totalAmount == order.getTotalAmount();
      @   ensures \result.items.size() == order.getItems().size();
      @ also
      @ public exceptional_behavior
      @   requires order == null;
      @   signals_only NullPointerException;
      @*/
    public static OrderResponse fromDomain(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems()
                        .stream()
                        .map(OrderItemResponse::fromDomain)
                        .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}

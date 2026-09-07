package com.felipe.orderservice.order.event;

import com.felipe.orderservice.order.domain.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        /*@ spec_public @*/ UUID eventId,
        /*@ spec_public @*/ String eventType,
        /*@ spec_public @*/ UUID orderId,
        /*@ spec_public @*/ UUID customerId,
        /*@ spec_public @*/ BigDecimal totalAmount,
        /*@ spec_public @*/ List<OrderCreatedItemEvent> items,
        /*@ spec_public @*/ Instant occurredAt
) {

    //@ public invariant eventId != null;
    //@ public invariant eventType != null && !eventType.isBlank();
    //@ public invariant orderId != null;
    //@ public invariant customerId != null;
    //@ public invariant totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) >= 0;
    //@ public invariant items != null;
    //@ public invariant occurredAt != null;

    /*@
      @ public normal_behavior
      @   requires order != null;
      @   ensures \result != null;
      @   ensures \result.orderId == order.getId();
      @   ensures \result.customerId == order.getCustomerId();
      @   ensures \result.totalAmount == order.getTotalAmount();
      @   ensures \result.items.size() == order.getItems().size();
      @   ensures \result.occurredAt != null;
      @ also
      @ public exceptional_behavior
      @   requires order == null;
      @   signals_only NullPointerException;
      @*/
    public static OrderCreatedEvent fromDomain(Order order) {
        return new OrderCreatedEvent(
                UUID.randomUUID(),
                "OrderCreated",
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getItems()
                        .stream()
                        .map(item -> new OrderCreatedItemEvent(
                                item.getProductId(),
                                item.getProductName(),
                                item.getUnitPrice(),
                                item.getQuantity(),
                                item.getSubtotal()
                        ))
                        .toList(),
                Instant.now()
        );
    }
}

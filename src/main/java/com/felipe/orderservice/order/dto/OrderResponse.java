package com.felipe.orderservice.order.dto;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {

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

package com.felipe.orderservice.order.dto;

import com.felipe.orderservice.order.domain.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        UUID productid,
        String productName,
        BigDecimal UnitPrice,
        Integer quantity,
        BigDecimal subtotal
) {

    public static OrderItemResponse fromDomain(OrderItem item){
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}

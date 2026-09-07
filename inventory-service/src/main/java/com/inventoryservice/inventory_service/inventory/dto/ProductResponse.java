package com.inventoryservice.inventory_service.inventory.dto;

import com.inventoryservice.inventory_service.inventory.domain.Product;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        Integer availableQuantity,
        Integer reservedQuantity,
        Instant createdAt,
        Instant updatedAt
) {

    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getAvailableQuantity(),
                product.getReservedQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
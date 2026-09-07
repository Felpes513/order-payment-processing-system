package com.inventoryservice.inventory_service.inventory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private Product(
            UUID id,
            String name,
            Integer availableQuantity,
            Integer reservedQuantity,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(String name, Integer availableQuantity) {
        validateName(name);
        validateAvailableQuantity(availableQuantity);

        Instant now = Instant.now();

        return new Product(
                UUID.randomUUID(),
                name,
                availableQuantity,
                0,
                now,
                now
        );
    }

    public void reserve(Integer quantity) {
        validateReservationQuantity(quantity);

        if (this.availableQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock available");
        }

        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
        this.updatedAt = Instant.now();
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }

        if (name.length() > 150) {
            throw new IllegalArgumentException("Product name cannot have more than 150 characters");
        }
    }

    private static void validateAvailableQuantity(Integer availableQuantity) {
        if (availableQuantity == null) {
            throw new IllegalArgumentException("Available quantity cannot be null");
        }

        if (availableQuantity < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative");
        }
    }

    private static void validateReservationQuantity(Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Reservation quantity cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Reservation quantity must be greater than zero");
        }
    }
}
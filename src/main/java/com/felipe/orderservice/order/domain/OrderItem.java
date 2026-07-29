package com.felipe.orderservice.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    //@ spec_public
    @Id
    private UUID id;

    //@ spec_public
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    //@ spec_public
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    //@ spec_public
    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    //@ spec_public
    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    //@ spec_public
    @Column(nullable = false)
    private Integer quantity;

    //@ spec_public
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    //@ public invariant id != null;
    //@ public invariant order != null;
    //@ public invariant productId != null;
    //@ public invariant productName != null && !productName.isBlank();
    //@ public invariant productName.length() <= 150;
    //@ public invariant unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
    //@ public invariant quantity != null && quantity > 0;
    //@ public invariant subtotal != null && subtotal.compareTo(BigDecimal.ZERO) >= 0;

    private OrderItem(UUID id, Order order, UUID productId, String productName, BigDecimal unitPrice, Integer quantity, BigDecimal subtotal, Instant createdAt) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.createdAt = createdAt;
    }

    /*@
      @ public normal_behavior
      @   requires order != null;
      @   requires productId != null;
      @   requires productName != null && !productName.isBlank();
      @   requires productName.length() <= 150;
      @   requires unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
      @   requires quantity != null && quantity > 0;
      @   ensures \result != null;
      @   ensures \result.order == order;
      @   ensures \result.productId == productId;
      @   ensures \result.productName == productName;
      @   ensures \result.unitPrice == unitPrice;
      @   ensures \result.quantity == quantity;
      @*/
    public static OrderItem create(Order order, UUID productId, String productName, BigDecimal unitPrice, Integer quantity) {
        validateOrder(order);
        validateProductId(productId);
        validateProductName(productName);
        validateUnitPrice(unitPrice);
        validateQuantity(quantity);

        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        return new OrderItem(
                UUID.randomUUID(),
                order,
                productId,
                productName,
                unitPrice,
                quantity,
                subtotal,
                Instant.now()
        );
    }

    private static void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }

    private static void validateProductId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
    }

    private static void validateProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }

        if (productName.length() > 150) {
            throw new IllegalArgumentException("Product name cannot have more than 150 characters");
        }
    }

    private static void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price cannot be null");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
    }

    private static void validateQuantity(Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}

/*
Por que usamos o FechType.LAZY?

Usamos para que o JPA não busque por dados desnecessários
 */

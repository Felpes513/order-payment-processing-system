package com.felipe.orderservice.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /*
    mappedBy indica que o relacionamento é controlado pelo atributo "order" em OrderItem.
    cascade = ALL faz com que operações no Order também sejam aplicadas aos seus itens.

    Relacionamento controlado por OrderItem.order.
    Ao salvar ou remover Order, seus itens acompanham a operação.
    */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private Order(UUID id, UUID customerId, OrderStatus status, BigDecimal totalAmount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer id cannot be null");
        }

        Instant now = Instant.now();

        return new Order(
                UUID.randomUUID(),
                customerId,
                OrderStatus.CREATED,
                BigDecimal.ZERO,
                now,
                now
        );
    }

    public void addItem(UUID productId, String productname, BigDecimal unitPrice, Integer quantity) {
        OrderItem item = OrderItem.create(
                this,
                productId,
                productname,
                unitPrice,
                quantity
        );
        this.items.add(item);
        recalculateTotal();
        this.updatedAt = Instant.now();
    }

    public void changeStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    public void updateTotal(BigDecimal totalAmount) {
        if (totalAmount == null) {
            throw new IllegalArgumentException("Total amount cannot be null");
        }

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }

        this.totalAmount = totalAmount;
        this.updatedAt = Instant.now();
    }

    public void recalculateTotal() {
        this.totalAmount = this.items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
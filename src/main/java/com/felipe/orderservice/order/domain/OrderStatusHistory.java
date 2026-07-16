package com.felipe.orderservice.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "order_status_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatusHistory {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 40)
    private OrderStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 40)
    private OrderStatus newStatus;

    @Column(length = 255)
    private String reason;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    private OrderStatusHistory(
            UUID id,
            Order order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String reason,
            UUID correlationId,
            Instant occurredAt
    ) {
        this.id = id;
        this.order = order;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.correlationId = correlationId;
        this.occurredAt = occurredAt;
    }

    public static OrderStatusHistory create(
            Order order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String reason,
            UUID correlationId
    ) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        if (reason != null && reason.length() > 255) {
            throw new IllegalArgumentException("Reason cannot have more than 255 characters");
        }

        return new OrderStatusHistory(
                UUID.randomUUID(),
                order,
                previousStatus,
                newStatus,
                reason,
                correlationId,
                Instant.now()
        );
    }
}
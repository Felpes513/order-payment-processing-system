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

    //@ spec_public
    @Id
    private UUID id;

    //@ spec_public
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    //@ spec_public
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 40)
    private OrderStatus previousStatus;

    //@ spec_public
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 40)
    private OrderStatus newStatus;

    //@ spec_public
    @Column(length = 255)
    private String reason;

    //@ spec_public
    @Column(name = "correlation_id")
    private UUID correlationId;

    //@ spec_public
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    //@ public invariant id != null;
    //@ public invariant order != null;
    //@ public invariant newStatus != null;
    //@ public invariant reason == null || reason.length() <= 255;
    //@ public invariant occurredAt != null;

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

    /*@
      @ public normal_behavior
      @   requires order != null;
      @   requires newStatus != null;
      @   requires reason == null || reason.length() <= 255;
      @   ensures \result != null;
      @   ensures \result.order == order;
      @   ensures \result.previousStatus == previousStatus;
      @   ensures \result.newStatus == newStatus;
      @   ensures \result.reason == reason;
      @   ensures \result.correlationId == correlationId;
      @   ensures \result.occurredAt != null;
      @ also
      @ public exceptional_behavior
      @   requires order == null
      @         || newStatus == null
      @         || (reason != null && reason.length() > 255);
      @   signals_only IllegalArgumentException;
      @*/
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

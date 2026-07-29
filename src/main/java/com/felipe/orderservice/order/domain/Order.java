package com.felipe.orderservice.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    //@ spec_public
    @Id
    private UUID id;

    //@ spec_public
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    //@ spec_public
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderStatus status;

    //@ spec_public
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    /*
    mappedBy indica que o relacionamento é controlado pelo atributo "order" em OrderItem.
    cascade = ALL faz com que operações no Order também sejam aplicadas aos seus itens.

    Relacionamento controlado por OrderItem.order.
    Ao salvar ou remover Order, seus itens acompanham a operação.
    */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    //@ public invariant id != null;
    //@ public invariant customerId != null;
    //@ public invariant status != null;
    //@ public invariant totalAmount != null;

    private Order(UUID id, UUID customerId, OrderStatus status, BigDecimal totalAmount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /*@
      @ public normal_behavior
      @   requires customerId != null;
      @   ensures \result != null;
      @   ensures \result.customerId == customerId;
      @   ensures \result.status == OrderStatus.CREATED;
      @   ensures \result.totalAmount.compareTo(BigDecimal.ZERO) == 0;
      @ also
      @ public exceptional_behavior
      @   requires customerId == null;
      @   signals_only IllegalArgumentException;
      @*/
    public static Order create(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer id cannot be null");
        }

        Instant now = Instant.now();

        Order order = new Order(
                UUID.randomUUID(),
                customerId,
                OrderStatus.CREATED,
                BigDecimal.ZERO,
                now,
                now
        );

        OrderStatusHistory history = OrderStatusHistory.create(
                order,
                null,
                OrderStatus.CREATED,
                "ORDER_CREATED",
                null
        );

        order.statusHistory.add(history);

        return order;
    }

    /*@
      @ public normal_behavior
      @   requires productId != null;
      @   requires productname != null && !productname.isBlank();
      @   requires productname.length() <= 150;
      @   requires unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
      @   requires quantity != null && quantity > 0;
      @   ensures items.size() == \old(items.size()) + 1;
      @   ensures totalAmount.compareTo(\old(totalAmount)) >= 0;
      @*/
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

    /*@
      @ public normal_behavior
      @   requires newStatus != null;
      @   requires reason == null || reason.length() <= 255;
      @   ensures status == newStatus;
      @   ensures statusHistory.size() == \old(statusHistory.size()) + 1;
      @ also
      @ public exceptional_behavior
      @   requires newStatus == null;
      @   signals_only IllegalArgumentException;
      @*/
    public void changeStatus(OrderStatus newStatus, String reason, UUID correlationId) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        OrderStatus previousStatus = this.status;

        this.status = newStatus;
        this.updatedAt = Instant.now();

        OrderStatusHistory history = OrderStatusHistory.create(
                this,
                previousStatus,
                newStatus,
                reason,
                correlationId
        );

        this.statusHistory.add(history);
    }

    public void changeStatus(OrderStatus newStatus){}

    /*@
      @ public normal_behavior
      @   requires totalAmount != null;
      @   requires totalAmount.compareTo(BigDecimal.ZERO) >= 0;
      @   ensures this.totalAmount == totalAmount;
      @ also
      @ public exceptional_behavior
      @   requires totalAmount == null
      @         || totalAmount.compareTo(BigDecimal.ZERO) < 0;
      @   signals_only IllegalArgumentException;
      @*/
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

    public List<OrderStatusHistory> getStatusHistory() {
        return Collections.unmodifiableList(this.statusHistory);
    }
}

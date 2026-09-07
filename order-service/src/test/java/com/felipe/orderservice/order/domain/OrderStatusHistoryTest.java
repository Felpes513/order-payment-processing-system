package com.felipe.orderservice.order.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStatusHistoryTest {

    @Test
    void shouldCreateStatusHistory() {
        Order order = Order.create(UUID.randomUUID());
        UUID correlationId = UUID.randomUUID();

        OrderStatusHistory history = OrderStatusHistory.create(
                order, OrderStatus.CREATED, OrderStatus.AWAITING_STOCK, "REQUEST_STOCK", correlationId
        );

        assertThat(history.getId()).isNotNull();
        assertThat(history.getOrder()).isSameAs(order);
        assertThat(history.getPreviousStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(history.getNewStatus()).isEqualTo(OrderStatus.AWAITING_STOCK);
        assertThat(history.getReason()).isEqualTo("REQUEST_STOCK");
        assertThat(history.getCorrelationId()).isEqualTo(correlationId);
        assertThat(history.getOccurredAt()).isNotNull();
    }

    @Test
    void shouldRejectInvalidStatusHistory() {
        Order order = Order.create(UUID.randomUUID());

        assertThatThrownBy(() -> OrderStatusHistory.create(null, null, OrderStatus.CREATED, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderStatusHistory.create(order, OrderStatus.CREATED, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderStatusHistory.create(order, OrderStatus.CREATED, OrderStatus.PAID, "x".repeat(256), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

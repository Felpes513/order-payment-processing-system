package com.felipe.orderservice.order.domain;

import com.felipe.orderservice.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void shouldCreateOrderWithInitialStateAndHistory() {
        UUID customerId = UUID.randomUUID();

        Order order = Order.create(customerId);

        assertThat(order.getId()).isNotNull();
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getCreatedAt()).isNotNull();
        assertThat(order.getUpdatedAt()).isNotNull();
        assertThat(order.getStatusHistory()).singleElement().satisfies(history -> {
            assertThat(history.getOrder()).isSameAs(order);
            assertThat(history.getPreviousStatus()).isNull();
            assertThat(history.getNewStatus()).isEqualTo(OrderStatus.CREATED);
            assertThat(history.getReason()).isEqualTo("ORDER_CREATED");
        });
    }

    @Test
    void shouldRejectNullCustomerId() {
        assertThatThrownBy(() -> Order.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer id cannot be null");
    }

    @Test
    void shouldAddItemsAndRecalculateTotal() {
        Order order = Order.create(UUID.randomUUID());

        order.addItem(UUID.randomUUID(), "Keyboard", new BigDecimal("150.50"), 2);
        order.addItem(UUID.randomUUID(), "Mouse", new BigDecimal("49.00"), 1);

        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getTotalAmount()).isEqualByComparingTo("350.00");
    }

    @Test
    void shouldChangeStatusAndRegisterHistory() {
        Order order = Order.create(UUID.randomUUID());
        UUID correlationId = UUID.randomUUID();

        order.changeStatus(OrderStatus.AWAITING_STOCK, "REQUEST_STOCK", correlationId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.AWAITING_STOCK);
        assertThat(order.getStatusHistory()).hasSize(2);
        assertThat(order.getStatusHistory().get(1)).satisfies(history -> {
            assertThat(history.getPreviousStatus()).isEqualTo(OrderStatus.CREATED);
            assertThat(history.getNewStatus()).isEqualTo(OrderStatus.AWAITING_STOCK);
            assertThat(history.getReason()).isEqualTo("REQUEST_STOCK");
            assertThat(history.getCorrelationId()).isEqualTo(correlationId);
        });
    }

    @Test
    void shouldRejectNullStatusAndOversizedReason() {
        Order order = Order.create(UUID.randomUUID());

        assertThatThrownBy(() -> order.changeStatus(null, "reason", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New status cannot be null");
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.PAID, "x".repeat(256), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reason cannot have more than 255 characters");
    }

    @Test
    void shouldUpdateTotalAndRejectInvalidAmounts() {
        Order order = Order.create(UUID.randomUUID());

        order.updateTotal(new BigDecimal("42.30"));

        assertThat(order.getTotalAmount()).isEqualByComparingTo("42.30");
        assertThatThrownBy(() -> order.updateTotal(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> order.updateTotal(new BigDecimal("-0.01")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void statusHistoryShouldBeUnmodifiable() {
        Order order = Order.create(UUID.randomUUID());

        assertThatThrownBy(() -> order.getStatusHistory().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldCancelOrder() {
        Order order = Order.create(UUID.randomUUID());

        order.cancel("CUSTOMER_REQUEST", UUID.randomUUID());

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.getStatusHistory()).hasSize(2);
    }

    @Test
    void shouldRejectCancellingCancelledOrCompletedOrder() {
        Order cancelled = Order.create(UUID.randomUUID());
        cancelled.cancel("first", null);
        Order completed = Order.create(UUID.randomUUID());
        completed.changeStatus(OrderStatus.COMPLETED, "done", null);

        assertThatThrownBy(() -> cancelled.cancel("again", null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Order is already cancelled");
        assertThatThrownBy(() -> completed.cancel("late", null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Completed order cannot be cancelled");
    }
}

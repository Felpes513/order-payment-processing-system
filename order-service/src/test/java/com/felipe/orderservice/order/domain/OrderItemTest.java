package com.felipe.orderservice.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @Test
    void shouldCreateItemAndCalculateSubtotal() {
        Order order = Order.create(UUID.randomUUID());
        UUID productId = UUID.randomUUID();

        OrderItem item = OrderItem.create(order, productId, "Monitor", new BigDecimal("999.90"), 2);

        assertThat(item.getId()).isNotNull();
        assertThat(item.getOrder()).isSameAs(order);
        assertThat(item.getProductId()).isEqualTo(productId);
        assertThat(item.getProductName()).isEqualTo("Monitor");
        assertThat(item.getUnitPrice()).isEqualByComparingTo("999.90");
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getSubtotal()).isEqualByComparingTo("1999.80");
        assertThat(item.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldRejectInvalidItemData() {
        Order order = Order.create(UUID.randomUUID());
        UUID productId = UUID.randomUUID();
        BigDecimal price = BigDecimal.TEN;

        assertThatThrownBy(() -> OrderItem.create(null, productId, "Product", price, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, null, "Product", price, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, null, price, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, " ", price, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, "x".repeat(151), price, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, "Product", null, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, "Product", new BigDecimal("-1"), 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, "Product", price, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> OrderItem.create(order, productId, "Product", price, 0)).isInstanceOf(IllegalArgumentException.class);
    }
}

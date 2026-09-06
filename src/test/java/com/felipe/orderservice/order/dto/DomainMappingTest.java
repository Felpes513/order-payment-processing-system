package com.felipe.orderservice.order.dto;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.domain.OrderStatus;
import com.felipe.orderservice.order.event.OrderCreatedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DomainMappingTest {

    @Test
    void shouldMapOrderAndItemsToResponse() {
        Order order = createOrderWithItem();

        OrderResponse response = OrderResponse.fromDomain(order);

        assertThat(response.id()).isEqualTo(order.getId());
        assertThat(response.customerId()).isEqualTo(order.getCustomerId());
        assertThat(response.status()).isEqualTo(OrderStatus.CREATED);
        assertThat(response.totalAmount()).isEqualByComparingTo("39.90");
        assertThat(response.createdAt()).isEqualTo(order.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(order.getUpdatedAt());
        assertThat(response.items()).singleElement().satisfies(item -> {
            assertThat(item.id()).isEqualTo(order.getItems().getFirst().getId());
            assertThat(item.productid()).isEqualTo(order.getItems().getFirst().getProductId());
            assertThat(item.productName()).isEqualTo("Book");
            assertThat(item.UnitPrice()).isEqualByComparingTo("19.95");
            assertThat(item.quantity()).isEqualTo(2);
            assertThat(item.subtotal()).isEqualByComparingTo("39.90");
        });
    }

    @Test
    void shouldMapOrderToCreatedEvent() {
        Order order = createOrderWithItem();

        OrderCreatedEvent event = OrderCreatedEvent.fromDomain(order);

        assertThat(event.eventId()).isNotNull();
        assertThat(event.eventType()).isEqualTo("OrderCreated");
        assertThat(event.orderId()).isEqualTo(order.getId());
        assertThat(event.customerId()).isEqualTo(order.getCustomerId());
        assertThat(event.totalAmount()).isEqualByComparingTo("39.90");
        assertThat(event.occurredAt()).isNotNull();
        assertThat(event.items()).singleElement().satisfies(item -> {
            assertThat(item.productId()).isEqualTo(order.getItems().getFirst().getProductId());
            assertThat(item.productName()).isEqualTo("Book");
            assertThat(item.unitPrice()).isEqualByComparingTo("19.95");
            assertThat(item.quantity()).isEqualTo(2);
            assertThat(item.subtotal()).isEqualByComparingTo("39.90");
        });
    }

    @Test
    void mappersShouldRejectNullDomainObjects() {
        assertThatThrownBy(() -> OrderResponse.fromDomain(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> OrderItemResponse.fromDomain(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> OrderCreatedEvent.fromDomain(null)).isInstanceOf(NullPointerException.class);
    }

    private Order createOrderWithItem() {
        Order order = Order.create(UUID.randomUUID());
        order.addItem(UUID.randomUUID(), "Book", new BigDecimal("19.95"), 2);
        return order;
    }
}

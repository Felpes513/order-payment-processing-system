package com.felipe.orderservice.order.controller;

import com.felipe.orderservice.order.domain.OrderStatus;
import com.felipe.orderservice.order.dto.CancelOrderRequest;
import com.felipe.orderservice.order.dto.CreateOrderRequest;
import com.felipe.orderservice.order.dto.OrderResponse;
import com.felipe.orderservice.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private OrderController controller;

    @BeforeEach
    void setUp() {
        controller = new OrderController(orderService);
    }

    @Test
    void shouldDelegateCreateOrder() {
        CreateOrderRequest request = new CreateOrderRequest(UUID.randomUUID(), List.of());
        OrderResponse expected = response(UUID.randomUUID(), OrderStatus.CREATED);
        when(orderService.createOrder(request)).thenReturn(expected);

        assertThat(controller.createOrder(request)).isSameAs(expected);
        verify(orderService).createOrder(request);
    }

    @Test
    void shouldDelegateFindById() {
        UUID id = UUID.randomUUID();
        OrderResponse expected = response(id, OrderStatus.CREATED);
        when(orderService.findById(id)).thenReturn(expected);

        assertThat(controller.findById(id)).isSameAs(expected);
        verify(orderService).findById(id);
    }

    @Test
    void shouldDelegateCancellation() {
        UUID id = UUID.randomUUID();
        CancelOrderRequest request = new CancelOrderRequest("reason");
        OrderResponse expected = response(id, OrderStatus.CANCELLED);
        when(orderService.cancelOrder(id, request)).thenReturn(expected);

        assertThat(controller.cancelOrder(id, request)).isSameAs(expected);
        verify(orderService).cancelOrder(id, request);
    }

    private OrderResponse response(UUID id, OrderStatus status) {
        Instant now = Instant.now();
        return new OrderResponse(id, UUID.randomUUID(), status, BigDecimal.ZERO, List.of(), now, now);
    }
}

package com.felipe.orderservice.order.service;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.domain.OrderStatus;
import com.felipe.orderservice.order.dto.CancelOrderRequest;
import com.felipe.orderservice.order.dto.CreateOrderItemRequest;
import com.felipe.orderservice.order.dto.CreateOrderRequest;
import com.felipe.orderservice.order.dto.OrderResponse;
import com.felipe.orderservice.order.event.OrderEventPublisher;
import com.felipe.orderservice.order.repository.OrderRepository;
import com.felipe.orderservice.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderEventPublisher);
    }

    @Test
    void shouldCreateAndSaveOrder() {
        UUID customerId = UUID.randomUUID();
        CreateOrderRequest request = new CreateOrderRequest(
                customerId,
                List.of(new CreateOrderItemRequest(
                        UUID.randomUUID(), "Headphones", new BigDecimal("120.00"), 2
                ))
        );
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.customerId()).isEqualTo(customerId);
        assertThat(response.status()).isEqualTo(OrderStatus.CREATED);
        assertThat(response.totalAmount()).isEqualByComparingTo("240.00");
        assertThat(response.items()).hasSize(1);
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisher).publishOrderCreated(any(Order.class));
    }

    @Test
    void shouldFindOrderById() {
        Order order = Order.create(UUID.randomUUID());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        OrderResponse response = orderService.findById(order.getId());

        assertThat(response.id()).isEqualTo(order.getId());
    }

    @Test
    void shouldThrowResourceNotFoundWhenOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Order not found");
    }

    @Test
    void shouldCancelAndSaveOrder() {
        Order order = Order.create(UUID.randomUUID());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderResponse response = orderService.cancelOrder(order.getId(), new CancelOrderRequest("CUSTOMER_REQUEST"));

        assertThat(response.status()).isEqualTo(OrderStatus.CANCELLED);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldRejectCancellationWhenOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.cancelOrder(id, new CancelOrderRequest("reason")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order not found");
    }
}

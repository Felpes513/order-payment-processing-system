package com.felipe.orderservice.order.event;

import com.felipe.orderservice.order.domain.Order;

public interface OrderEventPublisher {

    void publishOrderCreated(Order order);
}

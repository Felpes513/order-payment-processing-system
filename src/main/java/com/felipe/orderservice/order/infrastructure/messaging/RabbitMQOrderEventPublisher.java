package com.felipe.orderservice.order.infrastructure.messaging;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.event.OrderCreatedEvent;
import com.felipe.orderservice.order.event.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQOrderEventPublisher implements OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = OrderCreatedEvent.fromDomain(order);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                event
        );
    }
}
package com.felipe.orderservice.config;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.domain.OrderStatus;
import com.felipe.orderservice.order.domain.ProcessedEvent;
import com.felipe.orderservice.order.repository.OrderRepository;
import com.felipe.orderservice.order.repository.ProcessedEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.UUID;

//@Configuration
public class DataTestRunner {

    @Bean
    CommandLineRunner testOrderPersistence(
            OrderRepository orderRepository,
            ProcessedEventRepository processedEventRepository
    ) {
        return args -> {
            Order order = Order.create(UUID.randomUUID());

            order.addItem(
                    UUID.randomUUID(),
                    "MacBook Air M2",
                    new BigDecimal("10000.00"),
                    1
            );

            order.addItem(
                    UUID.randomUUID(),
                    "AirPods 2",
                    new BigDecimal("1000.00"),
                    2
            );

            UUID correlationId = UUID.randomUUID();

            order.changeStatus(
                    OrderStatus.AWAITING_STOCK,
                    "REQUEST_STOCK_RESERVATION",
                    correlationId
            );

            Order savedOrder = orderRepository.save(order);

            UUID eventId = UUID.randomUUID();

            ProcessedEvent processedEvent = ProcessedEvent.create(
                    eventId,
                    "StockReserved",
                    "stock-reserved-consumer"
            );

            processedEventRepository.save(processedEvent);

            System.out.println("Pedido salvo com sucesso!");
            System.out.println("ID do pedido: " + savedOrder.getId());
            System.out.println("Total: " + savedOrder.getTotalAmount());
            System.out.println("Quantidade de itens: " + savedOrder.getItems().size());
            System.out.println("Históricos: " + savedOrder.getStatusHistory().size());
            System.out.println("Evento processado salvo: " + eventId);
        };
    }
}
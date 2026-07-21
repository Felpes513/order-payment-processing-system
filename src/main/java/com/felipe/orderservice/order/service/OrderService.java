package com.felipe.orderservice.order.service;

import com.felipe.orderservice.order.domain.Order;
import com.felipe.orderservice.order.dto.CreateOrderItemRequest;
import com.felipe.orderservice.order.dto.CreateOrderRequest;
import com.felipe.orderservice.order.dto.OrderResponse;
import com.felipe.orderservice.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service //Essa anotação permite a injeção e gerenciamento em outras classes
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional //Essa tag trata cada chamada como uma transação unica
                  // Tudo que acontecer dentro desse método deve ser tratado como uma única operação no banco.
    public OrderResponse  createOrder(CreateOrderRequest request){
        Order order = Order.create(request.customerId());

        for (CreateOrderItemRequest item : request.items()){
            order.addItem(
                    item.productId(),
                    item.productName(),
                    item.unitPrice(),
                    item.quantity()
            );
        }

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.fromDomain(savedOrder);
    }
}

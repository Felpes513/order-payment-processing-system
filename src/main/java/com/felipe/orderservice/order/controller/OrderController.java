package com.felipe.orderservice.order.controller;

import com.felipe.orderservice.order.dto.CreateOrderRequest;
import com.felipe.orderservice.order.dto.OrderResponse;
import com.felipe.orderservice.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController // Essa tag diz que esta classe receba requisições HTTP e devolve em JSON
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Essa tag faz com que sempre que der certo retorne um status code 201
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }
}

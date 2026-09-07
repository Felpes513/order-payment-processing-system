package com.felipe.orderservice.order.domain;

public enum OrderStatus {

    CREATED,
    AWAITING_STOCK,
    STOCK_RESERVED,
    AWAITING_PAYMENT,
    PAID,
    PAYMENT_FAILED,
    CANCELLED,
    COMPLETED,
}

// Por que utilizar o ENUM?
/*
Sem o ENUM poderiamos ter no código um
private String status

Com isso qualquer valor seria aceito
Com o ENUM private OrderStatus, serão aceitos somente os estados definidos
 */

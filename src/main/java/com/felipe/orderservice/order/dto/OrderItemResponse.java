package com.felipe.orderservice.order.dto;

import com.felipe.orderservice.order.domain.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        /*@ spec_public @*/ UUID id,
        /*@ spec_public @*/ UUID productid,
        /*@ spec_public @*/ String productName,
        /*@ spec_public @*/ BigDecimal UnitPrice,
        /*@ spec_public @*/ Integer quantity,
        /*@ spec_public @*/ BigDecimal subtotal
) {

    //@ public invariant id != null;
    //@ public invariant productid != null;
    //@ public invariant productName != null && !productName.isBlank();
    //@ public invariant UnitPrice != null && UnitPrice.compareTo(BigDecimal.ZERO) >= 0;
    //@ public invariant quantity != null && quantity > 0;
    //@ public invariant subtotal != null && subtotal.compareTo(BigDecimal.ZERO) >= 0;

    /*@
      @ public normal_behavior
      @   requires item != null;
      @   ensures \result != null;
      @   ensures \result.id == item.getId();
      @   ensures \result.productid == item.getProductId();
      @   ensures \result.productName == item.getProductName();
      @   ensures \result.UnitPrice == item.getUnitPrice();
      @   ensures \result.quantity == item.getQuantity();
      @   ensures \result.subtotal == item.getSubtotal();
      @ also
      @ public exceptional_behavior
      @   requires item == null;
      @   signals_only NullPointerException;
      @*/
    public static OrderItemResponse fromDomain(OrderItem item){
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}

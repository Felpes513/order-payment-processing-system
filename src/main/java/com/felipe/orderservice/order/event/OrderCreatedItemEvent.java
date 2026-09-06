package com.felipe.orderservice.order.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedItemEvent(
        /*@ spec_public @*/ UUID productId,
        /*@ spec_public @*/ String productName,
        /*@ spec_public @*/ BigDecimal unitPrice,
        /*@ spec_public @*/ Integer quantity,
        /*@ spec_public @*/ BigDecimal subtotal
) {
    //@ public invariant productId != null;
    //@ public invariant productName != null && !productName.isBlank();
    //@ public invariant unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
    //@ public invariant quantity != null && quantity > 0;
    //@ public invariant subtotal != null && subtotal.compareTo(BigDecimal.ZERO) >= 0;
}

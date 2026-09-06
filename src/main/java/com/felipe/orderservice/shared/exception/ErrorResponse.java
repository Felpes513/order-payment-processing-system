package com.felipe.orderservice.shared.exception;

import java.time.Instant;

public record ErrorResponse(
        /*@ spec_public @*/ Instant timestamp,
        /*@ spec_public @*/ Integer status,
        /*@ spec_public @*/ String error,
        /*@ spec_public @*/ String message,
        /*@ spec_public @*/ String path
) {
    //@ public invariant timestamp != null;
    //@ public invariant status != null && status >= 400 && status <= 599;
    //@ public invariant error != null && !error.isBlank();
    //@ public invariant message != null;
    //@ public invariant path != null;
}

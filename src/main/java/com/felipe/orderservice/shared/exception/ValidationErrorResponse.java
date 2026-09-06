package com.felipe.orderservice.shared.exception;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        /*@ spec_public @*/ Instant timestamp,
        /*@ spec_public @*/ Integer status,
        /*@ spec_public @*/ String error,
        /*@ spec_public @*/ String message,
        /*@ spec_public @*/ String path,
        /*@ spec_public @*/ List<FieldErrorResponse> fieldErrors
) {
    //@ public invariant timestamp != null;
    //@ public invariant status != null && status == 400;
    //@ public invariant error != null && !error.isBlank();
    //@ public invariant message != null;
    //@ public invariant path != null;
    //@ public invariant fieldErrors != null;
}

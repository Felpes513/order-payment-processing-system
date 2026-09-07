package com.felipe.orderservice.shared.exception;

public record FieldErrorResponse(
        /*@ spec_public @*/ String field,
        /*@ spec_public @*/ String message
) {
    //@ public invariant field != null && !field.isBlank();
    //@ public invariant message != null;
}

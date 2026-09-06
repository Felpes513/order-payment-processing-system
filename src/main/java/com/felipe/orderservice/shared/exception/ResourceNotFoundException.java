package com.felipe.orderservice.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    /*@
      @ public normal_behavior
      @   requires message != null;
      @   ensures getMessage() == message;
      @*/
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

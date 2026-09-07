package com.felipe.orderservice.shared.exception;

public class BusinessException extends RuntimeException{

    /*@
      @ public normal_behavior
      @   requires message != null;
      @   ensures getMessage() == message;
      @*/
    public BusinessException(String message){
        super(message);
    }
}

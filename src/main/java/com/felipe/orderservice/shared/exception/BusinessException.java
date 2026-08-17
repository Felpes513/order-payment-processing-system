package com.felipe.orderservice.shared.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    }
}

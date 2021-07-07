package com.lyf.exception;

/**
 *  订单异常
 */


public class OrderException extends RuntimeException{

    public OrderException(String msg){
        super(msg);
    }
}

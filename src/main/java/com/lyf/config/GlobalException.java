package com.lyf.config;


import com.lyf.exception.OrderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {


    @ExceptionHandler(OrderException.class)
    @ResponseBody
    String handleException(OrderException e){
        return e.getMessage();
    }

}

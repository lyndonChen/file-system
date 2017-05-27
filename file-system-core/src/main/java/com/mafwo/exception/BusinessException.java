package com.mafwo.exception;

/**
 * Created by camelot on 2017/5/18.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message){
        super(message);
    }
}

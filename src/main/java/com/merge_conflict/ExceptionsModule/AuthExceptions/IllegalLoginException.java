package com.merge_conflict.ExceptionsModule.AuthExceptions;

public class IllegalLoginException extends RuntimeException{
    public IllegalLoginException(String message){
        super(message);
    }
}

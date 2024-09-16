package com.merge_conflict.ExceptionsModule.AuthExceptions;

public class IllegalNullArgumentException extends RuntimeException{
    public IllegalNullArgumentException(String message){
        super(message);
    }
}

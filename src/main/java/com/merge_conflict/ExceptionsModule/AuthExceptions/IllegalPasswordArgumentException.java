package com.merge_conflict.ExceptionsModule.AuthExceptions;

public class IllegalPasswordArgumentException extends RuntimeException{
    public IllegalPasswordArgumentException(String message){
        super(message);
    }
}


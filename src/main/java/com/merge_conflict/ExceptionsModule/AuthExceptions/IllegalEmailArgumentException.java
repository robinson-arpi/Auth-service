package com.merge_conflict.ExceptionsModule.AuthExceptions;

public class IllegalEmailArgumentException extends RuntimeException {
    public IllegalEmailArgumentException(String message) {
        super(message);
    }
}

package com.example.agentii_turism.exceptions;

/**
 * Throw this exception when invalid fields that needs to be inserted
 * in the database are occurred.
 */
public class InvalidFieldException extends BaseException{
    public InvalidFieldException(String msg) {
        super(msg);
    }
}

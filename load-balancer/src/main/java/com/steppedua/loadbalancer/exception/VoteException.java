package com.steppedua.loadbalancer.exception;

public class VoteException extends RuntimeException{
    public VoteException(String message) {
        super(message);
    }

    public VoteException(Throwable cause) {
        super(cause);
    }
}

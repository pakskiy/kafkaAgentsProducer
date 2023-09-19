package com.pakskiy.kafkaagentsproducer.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
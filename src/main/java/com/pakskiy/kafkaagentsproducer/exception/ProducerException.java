package com.pakskiy.kafkaagentsproducer.exception;

public class ProducerException extends RuntimeException {
    public ProducerException(String message) {
        super(message);
    }
}
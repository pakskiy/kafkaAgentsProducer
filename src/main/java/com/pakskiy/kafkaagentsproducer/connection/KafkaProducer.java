package com.pakskiy.kafkaagentsproducer.connection;

import com.pakskiy.kafkaagentsproducer.exception.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, String>> sendMessage(String topic, String key, String message) {
        try {
            return this.kafkaTemplate.send(topic, key, message);
        } catch (Exception e) {
            log.error("ERR_CONNECTION_SEND_MESSAGE: " + e);
            throw new ConnectionException("ERR_CONNECTION_SEND_MESSAGE");
        }
    }
}

package com.pakskiy.kafkaagentsproducer.service.impl;

import com.pakskiy.kafkaagentsproducer.connection.KafkaProducer;
import com.pakskiy.kafkaagentsproducer.exception.ProducerException;
import com.pakskiy.kafkaagentsproducer.model.MessageEntity;
import com.pakskiy.kafkaagentsproducer.repository.MessageRepository;
import com.pakskiy.kafkaagentsproducer.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final KafkaProducer producer;
    @Value("${spring.kafka.producer.topic.name}")
    private String outboundTopic;

    @Override
    public void save(List<MessageEntity> messageList) {
        try {
            messageRepository.saveAll(messageList);
        } catch (Exception e) {
            log.error("ERR_MSG_SRV_SAVE", e);
        }
    }

    public MessageEntity send(String message) {
        try {
            CompletableFuture<SendResult<String, String>> listenableFuture = producer.sendMessage(outboundTopic, "IN_KEY", String.format(message, 0));

            listenableFuture.thenAcceptAsync(result -> {
                log.info(String.format("INSERT MESSAGE: \ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().serializedValueSize()));
            });
            return MessageEntity.builder().body(message).build();
        } catch (Exception e) {
            log.error(e.toString(), "message: " + message);
            throw new ProducerException("Error sending message " + e);
        }
    }
}

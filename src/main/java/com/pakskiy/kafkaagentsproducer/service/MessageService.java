package com.pakskiy.kafkaagentsproducer.service;

import com.pakskiy.kafkaagentsproducer.model.MessageEntity;

import java.util.List;

public interface MessageService {
    void save(List<MessageEntity> messageList);
}

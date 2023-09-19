package com.pakskiy.kafkaagentsproducer.repository;

import com.pakskiy.kafkaagentsproducer.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}

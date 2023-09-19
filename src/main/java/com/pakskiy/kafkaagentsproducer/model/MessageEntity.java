package com.pakskiy.kafkaagentsproducer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class MessageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "id_message_sequence")
    @SequenceGenerator(name = "id_message_sequence", allocationSize = 1)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "body")
    private String body;
}
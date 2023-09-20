package com.pakskiy.kafkaagentsproducer;

import com.pakskiy.kafkaagentsproducer.model.MessageEntity;
import com.pakskiy.kafkaagentsproducer.repository.MessageRepository;
import com.pakskiy.kafkaagentsproducer.service.impl.AgentServiceImpl;
import com.pakskiy.kafkaagentsproducer.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaAgentsProducerApplication.class)
@ActiveProfiles("test")
@Testcontainers
@RequiredArgsConstructor
class KafkaAgentsProducerApplicationTests {
    @Autowired
    AgentServiceImpl agentService;
    @Autowired
    MessageServiceImpl messageService;
    @Autowired
    MessageRepository messageRepository;

    private static final DockerImageName dockerImageName = DockerImageName.parse("postgres:12.15");
    @Container
    static PostgreSQLContainer<?> postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer(dockerImageName)
            .withDatabaseName("stocksTestDb")
            .withUsername("postgres")
            .withPassword("123456").withReuse(true);

    @BeforeAll
    static void beforeAll() {
        postgresqlContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgresqlContainer.stop();
    }

    @Test
    void test_agent_list() {
        int count = 100;
        ReflectionTestUtils.setField(agentService, "AGENT_COUNT", count);
        assertEquals(agentService.getAgents().size(), count);

        count = 10;
        ReflectionTestUtils.setField(agentService, "AGENT_COUNT", count);
        assertEquals(agentService.getAgents().size(), count);

        count = 7;
        ReflectionTestUtils.setField(agentService, "TELEMETRY_COUNT", count);
        assertEquals(agentService.getAgents().get(0).getData().size(), count);

        count = 5;
        ReflectionTestUtils.setField(agentService, "TELEMETRY_COUNT", count);
        assertEquals(agentService.getAgents().get(0).getData().size(), count);
    }

    @Test
    void test_check_save_data() {
        ReflectionTestUtils.setField(agentService, "AGENT_COUNT", 2);
        ReflectionTestUtils.setField(agentService, "TELEMETRY_COUNT", 1);
        List<String> messageList = agentService.getMessageList(agentService.getAgents());
        String checkValue = messageList.get(0);
        List<MessageEntity> messageEntityList = messageList.parallelStream().map(e -> MessageEntity.builder().body(e).build()).toList();
        messageService.save(messageEntityList);
        List<MessageEntity> findAllList = messageRepository.findAll();
        Optional<MessageEntity> optionalMessageEntity = findAllList.stream().filter(e -> e.getBody().equals(checkValue)).findFirst();
        assertTrue(optionalMessageEntity.isPresent());
    }

}

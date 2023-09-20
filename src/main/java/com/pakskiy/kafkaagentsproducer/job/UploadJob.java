package com.pakskiy.kafkaagentsproducer.job;

import com.pakskiy.kafkaagentsproducer.model.MessageEntity;
import com.pakskiy.kafkaagentsproducer.service.impl.AgentServiceImpl;
import com.pakskiy.kafkaagentsproducer.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadJob {
    private final MessageServiceImpl messageService;
    private final AgentServiceImpl agentService;
    @Scheduled(fixedRateString = "${app.settings.download-timeout}", initialDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void startSchedule() {
        List<String> messageList = agentService.getMessageList(agentService.getAgents());
        List<MessageEntity> messageEntityList = messageList.parallelStream()
                .map(messageService::send).toList();
        messageService.save(messageEntityList);
    }
}
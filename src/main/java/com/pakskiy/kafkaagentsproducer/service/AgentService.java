package com.pakskiy.kafkaagentsproducer.service;

import com.pakskiy.kafkaagentsproducer.dto.AgentDto;

import java.util.List;

public interface AgentService {
    List<AgentDto> getAgents();
}

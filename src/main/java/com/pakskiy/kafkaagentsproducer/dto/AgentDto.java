package com.pakskiy.kafkaagentsproducer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AgentDto {
    @JsonProperty("agent_id")
    String agentId;
    @JsonProperty("manufacturer")
    String manufacturer;
    @JsonProperty("os")
    String os;
    @JsonProperty("data")
    List<TelemetryDto> data;
}
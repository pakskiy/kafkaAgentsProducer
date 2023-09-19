package com.pakskiy.kafkaagentsproducer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelemetryDto {
    @JsonProperty("uuid")
    String uuid;
    @JsonProperty("agent_id")
    String agentId;
    @JsonProperty("previous_message_time")
    long previousMessageTime;
    @JsonProperty("active_service")
    String activeService;
    @JsonProperty("quality_score")
    int qualityScore;
}

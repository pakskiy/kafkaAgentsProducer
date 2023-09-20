package com.pakskiy.kafkaagentsproducer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pakskiy.kafkaagentsproducer.dto.AgentDto;
import com.pakskiy.kafkaagentsproducer.dto.TelemetryDto;
import com.pakskiy.kafkaagentsproducer.service.AgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    @Value("${app.settings.agents-count}")
    private int AGENT_COUNT;

    @Value("${app.settings.telemetry-count}")
    private int TELEMETRY_COUNT;

    private final ObjectMapper objectMapper;

    @Override
    public List<AgentDto> getAgents() {
        var brand = new String[]{"Acer", "Alibaba Group", "Alpine", "Amd", "Apple", "Asus", "Benq", "Canon", "Casio", "Cisco Systems", "Clarion", "Dell", "Denon", "Emachines", "Epson", "Fuji Xerox", "Fujifilm", "Funai", "Gateway", "Gigabyte", "Haier Group", "Hewlett-packard", "Hitachi", "Huawei Technologies", "Ibm", "Intel", "Jvc", "Konica", "Kyocera", "Lenovo Group", "Marantz", "Melco (buffalo)", "Minolta", "Mitsubishi Electric", "Mouse Computer", "Msi", "Nec", "Nikon", "Nvidia", "Oki", "Olympus", "Oppo", "Packard Bell", "Panasonic", "Pentax", "Pioneer", "Planex", "Renesas", "Ricoh", "Rohm", "Sanyo", "Sgi", "Sharp", "Sii", "Sony", "Sun Microsystems", "Tencent Holdings", "Texas Instruments", "Toshiba", "Tsmc", "Unisys", "Via Technologies", "Vivo", "Xiaomi Corporation", "Zte"};
        var os = new String[]{"CentOS", "Chrome OS", "Debian", "Fedora", "Free BSD", "Mac OS", "MS Windows", "Solaris", "Ubuntu"};
        var service = new String[]{"Discord", "Douyin", "Facebook", "FaceTime", "iMessage", "IMO", "Instagram", "Josh", "Kuaishou", "Likee", "Line", "LinkedIn", "Meet", "Messenger", "Name", "Picsart", "Pinterest", "QQ", "Quora", "Qzone", "Reddit", "Skype", "Snapchat", "Stack Exchange", "Teams", "Telegram", "Tieba", "TikTok", "Tumblr", "Twitch", "Vevo", "Viber", "VK", "WeChat", "Weibo", "WhatsApp", "Twitter", "Xiaohongshu", "YouTube", "Zoom"};

        List<AgentDto> resultList = new ArrayList<>();

        for (int i = 0; i < AGENT_COUNT; i++) {
            String agentId = UUID.randomUUID().toString();

            List<TelemetryDto> telemetryList = new ArrayList<>();
            for (int j = 0; j < TELEMETRY_COUNT; j++) {
                telemetryList.add(TelemetryDto.builder()
                        .uuid(UUID.randomUUID().toString())
                        .agentId(agentId)
                        .previousMessageTime(getRandomDate())
                        .activeService(getRandomValue(service))
                        .qualityScore(new Random().nextInt(100 - 1) + 1)
                        .build()
                );
            }
            resultList.add(AgentDto.builder()
                    .agentId(agentId)
                    .manufacturer(getRandomValue(brand))
                    .os(getRandomValue(os))
                    .data(telemetryList)
                    .build()
            );
        }
        return resultList;
    }

    public List<String> getMessageList(List<AgentDto> agentDtoList) {
        List<String> messageList = new ArrayList<>();
        boolean isEmpty = false;
        if (!agentDtoList.isEmpty()) {
            try {
                while (!isEmpty) {
                    int size = agentDtoList.size();
                    int num = size == 1 ? 1 : new Random().nextInt(size - 1) + 1;

                    List<AgentDto> elementList = agentDtoList.subList(0, num);
                    try {
                        messageList.add(objectMapper.writeValueAsString(elementList));
                    } catch (Exception e) {
                        log.error("ERR_AGENT_SRV_GET_MSG_LIST_ITERATE", e);
                    }
                    agentDtoList.removeAll(elementList);
                    if (agentDtoList.size() == 0) {
                        isEmpty = true;
                    }
                }
            } catch (Exception e) {
                log.error("ERR_AGENT_SRV_GET_MSG_LIST_COMMON", e);
            }
        }
        return messageList;
    }

    @SafeVarargs
    private <T> T getRandomValue(T... items) {
        return items[new Random().nextInt(items.length)];
    }

    private long getRandomDate() {
        long startTimeInMillis = LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endTimeInMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return ThreadLocalRandom.current().nextLong(startTimeInMillis, endTimeInMillis);
    }
}
package com.yxw.producer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    // 普通消息topic
    private static final String TOPIC_NAME = "yangxiaowei";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("Producing message: " + message + " to topic: " + TOPIC_NAME);
        // 发送消息到指定Topic
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}

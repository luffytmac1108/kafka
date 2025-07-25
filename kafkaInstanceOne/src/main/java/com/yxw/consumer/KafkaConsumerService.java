package com.yxw.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    // @KafkaListener 注解将此方法标记为Kafka消息监听器
    // topics 指定要监听的Topic名称
    // groupId 指定消费者组ID，需要与application.properties中的配置一致
    @KafkaListener(topics = "yangxiaowei", groupId = "yangxiaowei")
    public void listen(String message, Acknowledgment acknowledgment) {
        log.info("Consumed message: " + message);
        // 手动提交偏移量
        acknowledgment.acknowledge();
    }
}

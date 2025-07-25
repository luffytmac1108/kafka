package com.yxw.producer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaSxProducerService {

    // 顺序消费的topic
    private static final String TOPIC_NAME = "shunxuxiaofei";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送带有Key的消息，确保相同Key的消息进入同一个分区。
     * @param key 消息的Key，用于保证顺序性。例如，用户ID、订单ID等。
     * @param message 消息内容
     */
    public void sendMessage(String key, String message) {
        log.info("Producing message with key: '" + key + "', value: '" + message + "' to topic: " + TOPIC_NAME);

        // 使用 kafkaTemplate.send(topic, key, value) 方法发送消息
        // Kafka会根据key的hash值将消息路由到对应的分区。
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, key, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully to partition " + result.getRecordMetadata().partition() +
                        " with offset " + result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send message: " + ex.getMessage());
            }
        });
    }
}

package com.yxw.controller;

import com.yxw.producer.KafkaProducerService;
import com.yxw.producer.KafkaSxProducerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaRestController {

    @Resource
    private KafkaProducerService producerService;

    @Resource
    private KafkaSxProducerService kafkaSxProducerService;

    @GetMapping("/send")
    public String sendMessageToKafka(@RequestParam("message") String message) {
        producerService.sendMessage(message);
        return "Message sent to Kafka: " + message;
    }

    @GetMapping("/sendSequentialMessage")
    public String sendSequentialMessageToKafka(@RequestParam("entityId") String entityId,
                                               @RequestParam("message") String message) {
        // 假设 entityId 是你需要保证顺序的维度，例如 userId, orderId 等
        kafkaSxProducerService.sendMessage(entityId, message);
        return "Sequential message sent to Kafka with key " + entityId + ": " + message;
    }
}

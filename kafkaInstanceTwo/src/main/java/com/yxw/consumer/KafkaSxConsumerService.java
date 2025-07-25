package com.yxw.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSxConsumerService {

    // topics 指定要监听的Topic名称
    // groupId 必须与application.properties中的配置一致
    @KafkaListener(topics = "shunxuxiaofei", groupId = "yangxiaowei")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        // record.key() 将是你在生产者中发送的那个 key (例如用户ID)
        // record.value() 是消息内容
        // record.partition() 是消息所在的分区
        // record.offset() 是消息在该分区中的偏移量
        log.info("Consumed message from partition " + record.partition() +
                ", offset " + record.offset() +
                ", key: '" + record.key() +
                "', value: '" + record.value() + "'");
        // 在这里处理你的业务逻辑
        // 例如：根据record.key() 来处理某个用户的订单事件
        try {
            // 模拟业务处理
            Thread.sleep(200);
            //log.info("Processing message for key: " + record.key() + " completed.");
            // 手动提交偏移量。只有当消息被成功处理后才提交。
            // 这确保了即使消费者实例崩溃，重新启动后也能从上次成功处理的地方继续消费，
            // 从而避免重复消费或丢失消息。
            acknowledgment.acknowledge();
            //log.info("Acknowledged message from partition " + record.partition() + ", offset " + record.offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //log.info("Message processing interrupted for key: " + record.key() + ", error: " + e.getMessage());
            // 通常这里不会提交，让消息在下次重新消费
        } catch (Exception e) {
            // 如果处理失败，通常不提交，让下次重新消费，或者发送到死信队列 (DLQ)
            //log.info("Error processing message for key: " + record.key() + ", error: " + e.getMessage());
        }
    }
}

package com.yxw.partitions;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {

    private static final Logger log = LoggerFactory.getLogger(CustomPartitioner.class);

    // 这个方法在分区器初始化时被调用，可以用于读取配置
    @Override
    public void configure(Map<String, ?> configs) {
        // 可以在这里读取配置，例如从 application.properties 传递过来的自定义参数
        System.out.println("CustomPartitioner configured with: " + configs);
    }

    /**
     * 这是核心方法，决定消息发送到哪个分区。
     *
     * @param topic Topic 名称
     * @param key 消息的 Key (可能为 null)
     * @param keyBytes Key 的字节数组 (如果 Key 为 null，则为 null)
     * @param value 消息的 Value (可能为 null)
     * @param valueBytes Value 的字节数组 (如果 Value 为 null，则为 null)
     * @param cluster 集群元数据，包含 Topic 的分区信息
     * @return 目标分区号
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 获取 Topic 的分区信息
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if (keyBytes == null) {
            // 如果 Key 为 null，可以模仿默认分区器进行轮询，或者抛出异常要求必须有 Key
            // 这里我们简单地返回一个随机分区，或者你可以实现轮询逻辑
            // 建议：如果你的业务要求严格顺序，Key 不应为 null
            log.info("No key provided, sending to a random partition for topic: " + topic);
            // 示例：随机分配
            return (int) (Math.random() * numPartitions);
        }

        // --- 这里是你自定义分区逻辑的关键 ---
        // 目前我这个示例里面我创建的 topic 有3个分区，命令如下：
        // .\bin\windows\kafka-topics.bat --create --topic shunxuxiaofei --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

        // 示例 1: 基于 Key 的字符串内容进行简单判断
        String keyString = (String) key;
        if (keyString.startsWith("VIP_")) {
            // 将所有 VIP 用户消息发送到特定分区，例如分区 0
            return 0;
        } else if (keyString.startsWith("GUEST_")) {
            // 将所有访客消息发送到另一个特定分区，例如分区 1
            return 1;
        } else if (keyString.startsWith("ADMIN_")){
            // 将所有管理员消息发送到另一个特定分区，例如分区 2
            return 2;
        }

        //默认返回随机分区
        return (int) (Math.random() * numPartitions);
    }

    // 在分区器关闭时被调用，用于资源清理
    @Override
    public void close() {
        log.info("CustomPartitioner closed.");
    }
}

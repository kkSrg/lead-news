package com.shawn.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author shawn
 * @date 2023年 01月 25日 20:10
 */

public class producer {
    public static void main(String[] args) {
        //1.kafka的配置信息
        Properties properties = new Properties();
        //kafka的连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        //发送失败，失败的重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,5);
        //消息key的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //消息value的序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //2.生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        //封装发送的消息
        ProducerRecord<String,String> record = new ProducerRecord<String, String>("leadNews-topic","100001","hello kafka");

        //3.同步发送消息
        try {
            long offset = producer.send(record).get().offset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //异步发送消息
        producer.send(record, (recordMetadata, e) -> {
            if (e!=null){
                System.out.println("异常抛出");
            }
            System.out.println(recordMetadata.offset());
        });

        //4.关闭消息通道，必须关闭，否则消息发送不成功
        producer.close();
    }
}
class consumer{
    public static void main(String[] args) {
        //1.kafka的配置信息
        Properties properties = new Properties();
        //kafka的连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"group2");
        //消息key的序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        //消息value的序列化器
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        //创建消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //订阅消息主题
        consumer.subscribe(Collections.singletonList("leadNews-topic"));

        while (true) {
            //获取消息
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.key());
                System.out.println(record.value());
            }
        }
    }
}

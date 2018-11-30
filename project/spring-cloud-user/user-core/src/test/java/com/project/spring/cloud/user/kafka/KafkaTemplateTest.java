package com.project.spring.cloud.user.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by wencheng on 2018/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class KafkaTemplateTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void send() throws Exception {
        ListenableFuture listenableFuture = kafkaTemplate.send("test-topic", "Hello");
        listenableFuture.addCallback(result -> {
            log.info("kafka 消息发送成功, {}", JSON.toJSONString(result));
        }, ex -> {
            log.error("kafka 消息发送失败", ex);
        });
    }

}

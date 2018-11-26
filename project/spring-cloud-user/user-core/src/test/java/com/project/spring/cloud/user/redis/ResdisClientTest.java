package com.project.spring.cloud.user.redis;

import com.project.spring.cloud.common.cache.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wencheng on 2018/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResdisClientTest {

    @Autowired
    private RedisClient redisClient;

    @Test
    public void get() {
        String key = redisClient.get("key");
        System.out.println(key);
    }

}

package pers.mk.redis.work.springbootredis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void get(){
        redisTemplate.opsForValue().set("mk","马坤");
        String mk = redisTemplate.opsForValue().get("mk");
        System.out.println(mk);
    }
}

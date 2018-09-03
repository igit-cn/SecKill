package com.cloudSeckill.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    public static final String VALIDATE_CODE = "VALIDATE_CODE";//用户验证码
    
    @Autowired private RedisTemplate<String, Object> redisTemplate;
    @Autowired private StringRedisTemplate stringRedisTemplate;
    
    private String namespace = "wc:%s";
    
    public void set(String k, Object v, long time) {
        String key = String.format(namespace, k);
        if (v instanceof String && stringRedisTemplate != null) {
            stringRedisTemplate.opsForValue().set(key, (String) v);
            if (time > 0) stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, v);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    public void set(String k, Object v) {
        set(k, v, -1);
    }

    public boolean contains(String key) {
        return redisTemplate.hasKey(String.format(namespace, key)) || stringRedisTemplate.hasKey(String.format(namespace, key));
    }

    public String getStr(String k) {
        return stringRedisTemplate.opsForValue().get(String.format(namespace, k));
    }
    
    public <T> T getObject(String k) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return (T) valueOps.get(String.format(namespace, k));
    }

    public void removeStr(String key) {
        stringRedisTemplate.delete(String.format(namespace, key));
    }


    public void removeObj(String key) {
        redisTemplate.delete(String.format(namespace, key));
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(String.format(namespace, pattern));
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(String.format(namespace, key), delta);
    }

    public Double increment(String key, double delta) {
        return redisTemplate.opsForValue().increment(String.format(namespace, key), delta);
    }

    public void addSetItem(String key, Object value) {
        redisTemplate.opsForSet().add(String.format(namespace, key), value);
    }

    public boolean isSetMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(String.format(namespace, key), value);
    }

    public boolean reMoveSetMember(String key, Object value) {
        Long lRemove = redisTemplate.opsForSet().remove(String.format(namespace, key), value);
        return lRemove > 0;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void sendEvent(String channel, String msg) {
        stringRedisTemplate.convertAndSend(channel, msg);
    }
}

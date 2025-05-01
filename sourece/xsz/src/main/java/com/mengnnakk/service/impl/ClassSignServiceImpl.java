package com.mengnnakk.service.impl;

import com.mengnnakk.mapper.SignRecordMapper;
import com.mengnnakk.service.ClassSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service
public class ClassSignServiceImpl implements ClassSignService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SignRecordMapper signRecordMapper;

    @Autowired
    public ClassSignServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public String createTask(String taskId, LocalDateTime start, LocalDateTime end) {
        long startTs = start.toEpochSecond(ZoneOffset.UTC);
        long endTs = end.toEpochSecond(ZoneOffset.UTC);
        String code = String.format("%06d",new Random().nextInt(1_000_000));

        Map<String,String> meta = new HashMap<>();
        meta.put("start", String.valueOf(startTs));
        meta.put("end", String.valueOf(endTs));
        meta.put("code", code);
        meta.put("status", "OPEN");
        redisTemplate.opsForHash().putAll("sign:task:" + taskId, meta);
        return code;
    }

    @Override
    public void closeTask(String taskId) {
        redisTemplate.opsForHash().put("sign:task"+taskId,"status","Closed");
    }

    @Override
    public Set<String> getRecords(String taskId) {
        return redisTemplate.opsForSet().members("sign:record"+taskId);
    }

    @Override
    public boolean sign(String taskId, String studentId, String code) {
        Map<Object,Object> meta = redisTemplate.opsForHash().entries("sign:task"+taskId);
        if (meta.isEmpty()){
            log.warn("任务不存在{}"+taskId);
            return false;
        }
        long now = Instant.now().getEpochSecond();
        long startTs = Long.parseLong((String) meta.get("start"));
        long endTs   = Long.parseLong((String) meta.get("end"));
        String status = (String) meta.get("status");
        String realCode = (String) meta.get("code");

        if (!"Open".equals(status)||now<startTs|now>endTs){
            log.warn("签到已结束或签到不存在:{}"+taskId);
            return false;
        }
        if (code!=null&&!code.equals(realCode)){
            log.warn("验证码不正确{}!={}"+code,realCode);
            return false;
        }
        Boolean already = redisTemplate.opsForSet().isMember("sign:record"+taskId,studentId);
        if (Boolean.TRUE.equals(already)){
            return true;
        }
        redisTemplate.opsForSet().add("sign:record"+taskId+studentId);
        return true;

    }

    @Override
    public List<String> getAbsentList(String taskId, List<String> classStudentIds) {
        return null;
    }
}


package com.mengnnakk.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mengnnakk.entry.sign.SignTask;
import com.mengnnakk.mapper.SignRecordMapper;
import com.mengnnakk.mapper.SignTaskMapper;
import com.mengnnakk.service.ClassSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassSignServiceImpl extends ServiceImpl<SignTaskMapper,SignTask> implements ClassSignService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SignRecordMapper signRecordMapper;
    private static final String TASK_PREFIX = "sign:task:";
    private static final String RECORD_PREFIX = "sign:record:";

    @Autowired
    public ClassSignServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean createTask(LocalDateTime start, LocalDateTime end) {
        String taskId = UUID.randomUUID().toString();
        long startTs = start.toEpochSecond(ZoneOffset.UTC);
        long endTs = end.toEpochSecond(ZoneOffset.UTC);
        String code = String.format("%06d",new Random().nextInt(1_000_000));

        SignTask task = new SignTask();
        task.setTaskId(taskId);
        task.setCode(code);
        task.setStatus("Open");
        task.setStartTime(start);
        task.setEndTime(end);
        baseMapper.insert(task);

        redisTemplate.opsForValue().set(TASK_PREFIX+taskId,task.getCode(), Duration.between(LocalDateTime.now(),end));

        return true;
    }

    @Override
    public void closeTask(String taskId) {

        SignTask task = baseMapper.selectById(taskId);
        if (task!=null){
            task.setStatus("Close");
            baseMapper.updateById(task);
        }
       redisTemplate.delete(TASK_PREFIX+taskId);
    }

    @Override
    public Set<String> getRecords(String taskId) {
        Set<String> signed = redisTemplate.opsForSet().members(RECORD_PREFIX+taskId);
        if (signed==null||signed.isEmpty()){
            signed = signRecordMapper.findSignedStudentIds(taskId);
            if (signed!=null){
                redisTemplate.opsForSet().add(RECORD_PREFIX+taskId,signed.toArray(new String[0]));
            }
        }

        return redisTemplate.opsForSet().members(RECORD_PREFIX+taskId);
    }

    @Override
    public boolean sign(String taskId, String studentId, String code) {
        Map<Object,Object> meta = redisTemplate.opsForHash().entries(TASK_PREFIX+taskId);
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
        Boolean already = redisTemplate.opsForSet().isMember(RECORD_PREFIX+taskId,studentId);
        if (Boolean.TRUE.equals(already)){
            return true;
        }
        redisTemplate.opsForSet().add(RECORD_PREFIX+taskId+studentId);
        return true;

    }

    /**
     * 查看签到情况，返回缺勤同学
     * @param taskId
     * @param classStudentIds
     * @return
     */

    @Override
    public List<String> getAbsentList(String taskId, List<String> classStudentIds) {

        Set<String> signed = getRecords(taskId);
        return classStudentIds.stream()
                        .filter(id->!signed.contains(id)
        ).collect(Collectors.toList());
    }

    /**
     * 返回签到的学生
     * @param taskId
     * @param classStudentIds
     * @return
     */

    @Override
    public List<String> getSignList(String taskId, List<String> classStudentIds) {
        Set<String> signed = getRecords(taskId);
        return classStudentIds.stream()
                .filter(id->signed.contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public String getCode(String taskId) {
        String code  =redisTemplate.opsForValue().get(TASK_PREFIX+taskId);
        if (code==null){
            SignTask task  =baseMapper.selectById(taskId);
            if (task!=null&&"Open".equals(task.getStatus())){
                code = task.getCode();
            }
        }
        return code;
    }


}


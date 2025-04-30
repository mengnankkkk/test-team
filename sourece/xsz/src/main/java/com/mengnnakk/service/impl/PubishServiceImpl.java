package com.mengnnakk.service.impl;

import com.mengnnakk.service.PubishService;
import javafx.scene.effect.SepiaTone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Service
@Slf4j
public class PubishServiceImpl implements PubishService {
    private StringRedisTemplate redisTemplate;

    @Override
    public void scheduleAssignment(String assignmentId, LocalDateTime publishTime) {
        long score = publishTime.toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForZSet().add("assignment_delay_queue",assignmentId,score);
    }

    /**
     * 发布作业
     */
    @Scheduled(fixedRate = 5000)
    @Override
    public void publishDueAssignments() {
        long now = Instant.now().getEpochSecond();
        Set<String> dueAssignments = redisTemplate.opsForZSet()
                .rangeByScore("assignment_delay_queue",0,now);
        for (String assignmentId:dueAssignments){
            log.info("发布作业"+assignmentId);
            redisTemplate.opsForZSet().remove("assignment_delay_queue",assignmentId);
        }
    }

    @Override
    public void notifyStudents(String assignmentId) {
        redisTemplate.convertAndSend("homework_channel","作业已经发布"+assignmentId);
    }

    /**
     * 提交作业
     * @param studentId
     * @param assignmentId
     * @param content
     */

    @Override
    public void submitAssignment(String studentId, String assignmentId, String content) {
        Map<String,String> fields = new HashMap<>();
        fields.put("assignmentId", assignmentId);
        fields.put("content", content);
        fields.put("studentId", studentId);
        redisTemplate.opsForStream().add("assignment_stream",fields);
    }

    @Scheduled(fixedRate = 2000)
    @Override
    public void consumeAssignmentSubmissions() {
    List<MapRecord<String,Object,Object>> message = redisTemplate.opsForStream()
            .read(StreamReadOptions.empty().count(10).block(Duration.ofMillis(100)), StreamOffset.fromStart("assignment_stream"));
    for (MapRecord<String,Object,Object> record:message){
        Map<Object,Object> submission = record.getValue();
        log.info("处理提交"+submission);
    }
    }
}

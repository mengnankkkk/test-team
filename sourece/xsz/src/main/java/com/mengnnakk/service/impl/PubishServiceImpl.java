package com.mengnnakk.service.impl;

import com.mengnnakk.service.PubishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.Style;
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

    public PubishServiceImpl(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * 定时发布作业，设置截至时间
     * @param assignmentId
     * @param publishTime
     * @param dateline
     */

    @Override
    public void scheduleAssignment(String assignmentId, LocalDateTime publishTime,LocalDateTime dateline) {
        long score = publishTime.toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForZSet().add("assignment_delay_queue",assignmentId,score);

        //截至时间
        redisTemplate.opsForHash().put("assignment_deadline",assignmentId,String.valueOf(dateline.toEpochSecond(ZoneOffset.UTC)));//HSET assignment_deadline assignmentId deadline_timestamp

    }

    /**
     * 发布作业,定时发布
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
            redisTemplate.convertAndSend("homework_channel","作业已经发布"+assignmentId);//PUBLISH channel message

        }
    }



    /**
     * 提交作业
     * @param studentId
     * @param assignmentId
     * @param content
     */

    @Override
    public void submitAssignment(String studentId, String assignmentId, String content) {
        long now = Instant.now().getEpochSecond();
        String deadlineStr = (String) redisTemplate.opsForHash().get("assignment_deadline",assignmentId);
        if (deadlineStr!=null&&now>Long.parseLong(deadlineStr)){
            log.warn("学生 {} 提交作业 {} 已超时",studentId,assignmentId);
            return;
        }

        boolean isCorrect = content.contains("正确");
        if (!isCorrect){
            redisTemplate.opsForSet().add("wrongbook:"+studentId,assignmentId);
        }
        redisTemplate.opsForHash().increment("stats:total"+studentId,"total",1);
        if (isCorrect){
            redisTemplate.opsForHash().increment("stats:total"+studentId,"correct",1);//HINCRBY stats:total:<studentId> correct 1


        }

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
    //XREAD COUNT 10 BLOCK 100 STREAMS assignment_stream 0
    for (MapRecord<String,Object,Object> record:message){
        Map<Object,Object> data = record.getValue();
        String studentId = (String) data.get("studentId");

        Object correct = redisTemplate.opsForHash().get("stats:total"+studentId,"correct");
        Object total  =redisTemplate.opsForHash().get("stats:total"+studentId,"total");
        //计算正确率
        double rate = 0;
        if (correct!=null&&total!=null){
            long c = Long.parseLong(correct.toString());
            long t = Long.parseLong(total.toString());
            rate = t==0?0:(double) c/t;
        }
        redisTemplate.opsForZSet().add("rank:accuracy",studentId,rate);
        log.info("处理提交:{},正确率{}:"+studentId,rate);
    }
    }

    /**
     * 正确率排行榜
     * @return
     */

    @Override
    public Set<String> getAccuracyRanking() {
        return redisTemplate.opsForZSet().reverseRange("ranking:accuracy",0,9);
    }

    @Override
    public Set<String> getWrongBook(String studentId) {
        return redisTemplate.opsForSet().members("wrongbook"+studentId);
    }
}

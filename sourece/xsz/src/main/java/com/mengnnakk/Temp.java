package com.mengnnakk;

public class Temp {

package com.mengnnakk.service.impl;

import com.mengnnakk.service.PubishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

    @Service
    @Slf4j
    public class PubishServiceImpl implements PubishService {

        private final StringRedisTemplate redisTemplate;

        public PubishServiceImpl(StringRedisTemplate redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        /**
         * 添加到延时发布队列，并设置截至时间（用于限时作业）
         */
        @Override
        public void scheduleAssignment(String assignmentId, LocalDateTime publishTime, LocalDateTime deadline) {
            long publishScore = publishTime.toEpochSecond(ZoneOffset.UTC);
            redisTemplate.opsForZSet().add("assignment_delay_queue", assignmentId, publishScore);

            // 存储截止时间
            redisTemplate.opsForHash().put("assignment_deadline", assignmentId, String.valueOf(deadline.toEpochSecond(ZoneOffset.UTC)));
        }

        /**
         * 定时发布作业
         */
        @Scheduled(fixedRate = 5000)
        @Override
        public void publishDueAssignments() {
            long now = Instant.now().getEpochSecond();
            Set<String> dueAssignments = redisTemplate.opsForZSet()
                    .rangeByScore("assignment_delay_queue", 0, now);
            for (String assignmentId : dueAssignments) {
                log.info("发布作业：" + assignmentId);
                redisTemplate.opsForZSet().remove("assignment_delay_queue", assignmentId);
                redisTemplate.convertAndSend("homework_channel", "作业已发布：" + assignmentId);
            }
        }

        /**
         * 提交作业：检查是否超时 + 错题记录 + 正确率计算
         */
        @Override
        public void submitAssignment(String studentId, String assignmentId, String content) {
            long now = Instant.now().getEpochSecond();
            String deadlineStr = (String) redisTemplate.opsForHash().get("assignment_deadline", assignmentId);

            if (deadlineStr != null && now > Long.parseLong(deadlineStr)) {
                log.warn("学生 {} 提交作业 {} 已超时", studentId, assignmentId);
                return;
            }

            // TODO: 实际应根据答案评判正误
            boolean isCorrect = content.contains("正确");

            if (!isCorrect) {
                // 添加到错题本（Set）
                redisTemplate.opsForSet().add("wrongbook:" + studentId, assignmentId);
            }

            // 正确率计分：统计学生总答题与正确数
            redisTemplate.opsForHash().increment("stats:total:" + studentId, "total", 1);
            if (isCorrect) {
                redisTemplate.opsForHash().increment("stats:total:" + studentId, "correct", 1);
            }

            // 发送作业到流中处理
            Map<String, String> fields = new HashMap<>();
            fields.put("assignmentId", assignmentId);
            fields.put("studentId", studentId);
            fields.put("content", content);
            redisTemplate.opsForStream().add("assignment_stream", fields);
        }

        /**
         * 消费作业流，计算正确率并更新排行榜
         */
        @Scheduled(fixedRate = 2000)
        @Override
        public void consumeAssignmentSubmissions() {
            List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                    .read(StreamReadOptions.empty().count(10).block(Duration.ofMillis(100)),
                            StreamOffset.fromStart("assignment_stream"));

            for (MapRecord<String, Object, Object> record : messages) {
                Map<Object, Object> data = record.getValue();
                String studentId = (String) data.get("studentId");

                Object correct = redisTemplate.opsForHash().get("stats:total:" + studentId, "correct");
                Object total = redisTemplate.opsForHash().get("stats:total:" + studentId, "total");

                double rate = 0;
                if (correct != null && total != null) {
                    long c = Long.parseLong(correct.toString());
                    long t = Long.parseLong(total.toString());
                    rate = t == 0 ? 0 : (double) c / t;
                }

                // 更新排行榜（ZSet，score=正确率）
                redisTemplate.opsForZSet().add("ranking:accuracy", studentId, rate);
                log.info("处理提交：{} 正确率={}", studentId, rate);
            }
        }

        /**
         * 获取学生错题本
         */
        public Set<String> getWrongBook(String studentId) {
            return redisTemplate.opsForSet().members("wrongbook:" + studentId);
        }

        /**
         * 获取正确率排行榜
         */
        public Set<String> getAccuracyRanking() {
            return redisTemplate.opsForZSet().reverseRange("ranking:accuracy", 0, 9);
        }
    }




}
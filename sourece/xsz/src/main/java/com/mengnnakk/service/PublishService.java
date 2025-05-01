package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;

import java.time.LocalDateTime;
import java.util.Set;

public interface PublishService  {
    public void scheduleAssignment(String assignmentId, LocalDateTime publishTime,LocalDateTime dateline);
    public void publishDueAssignments();
    public void submitAssignment(String studentId, String assignmentId, String content);
    public void consumeAssignmentSubmissions();
    public Set<String> getAccuracyRanking();

    public Set<String> getWrongBook(String studentId);






}

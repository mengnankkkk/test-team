package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;

import java.time.LocalDateTime;

public interface PubishService  {
    public void scheduleAssignment(String assignmentId, LocalDateTime publishTime);
    public void publishDueAssignments();
    public void notifyStudents(String assignmentId);
    public void submitAssignment(String studentId, String assignmentId, String content);
    public void consumeAssignmentSubmissions();





}

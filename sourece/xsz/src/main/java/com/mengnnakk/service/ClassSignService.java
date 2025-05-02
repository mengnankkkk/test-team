package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;
import com.mengnnakk.entry.sign.SignTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ClassSignService extends IService<SignTask>  {
    boolean createTask(LocalDateTime start, LocalDateTime end);
    void closeTask(String taskId);
    Set<String> getRecords(String taskId);
    boolean sign(String taskId, String studentId, String code);
    List<String> getAbsentList(String taskId,List<String> classStudentIds);
    List<String> getSignList(String taskId,List<String> classStudentIds);
    public String getCode(String taskId);
}

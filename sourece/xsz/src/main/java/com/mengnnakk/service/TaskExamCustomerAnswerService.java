package com.mengnnakk.service;


import com.baomidou.mybatisplus.service.IService;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.TaskExamCustomerAnswer;
import io.swagger.models.auth.In;
import javafx.concurrent.Task;

import java.util.Date;
import java.util.List;

public interface TaskExamCustomerAnswerService extends IService<TaskExamCustomerAnswer> {
    void insertOrUpdate(ExamPaper examPaper, ExamPaperAnswer examPaperAnswer, Date now);
    TaskExamCustomerAnswer selectByUid(Integer tid,Integer uid);
    List<TaskExamCustomerAnswer> selectByTUid(List<Integer> taskIds,Integer uid);
}
package com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.TaskExamCustomerAnswer;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.task.TaskItemAnswerObject;
import com.mengnnakk.mapper.TaskExamCustomerAnswerMapper;
import com.mengnnakk.service.TaskExamCustomerAnswerService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.JsonUtil;
import io.swagger.models.auth.In;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TaskExamCustomerAnswerImpl extends ServiceImpl<TaskExamCustomerAnswerMapper ,TaskExamCustomerAnswer>implements TaskExamCustomerAnswerService {

    private final TaskExamCustomerAnswerMapper taskExamCustomerAnswerMapper;
    private final TextContentService textContentService;

    @Autowired
    public TaskExamCustomerAnswerImpl(TaskExamCustomerAnswerMapper taskExamCustomerAnswerMapper, TextContentService textContentService) {
        this.taskExamCustomerAnswerMapper = taskExamCustomerAnswerMapper;
        this.textContentService = textContentService;
    }

    @Override
    public List<TaskExamCustomerAnswer> selectByTUid(List<Integer> taksIds,Integer uid){
        return taskExamCustomerAnswerMapper.selectByUid(taksIds,uid);
    }

    @Override
    public TaskExamCustomerAnswer selectByUid(Integer tid,Integer uid){
        return taskExamCustomerAnswerMapper.getByUid(tid,uid);
    }
    @Override
    public void insertOrUpdate(ExamPaper examPaper, ExamPaperAnswer examPaperAnswer, Date now){
        Integer taskId = examPaper.getTaskExamId();
        Integer userId = examPaperAnswer.getCreateUser();
        TaskExamCustomerAnswer taskExamCustomerAnswer = taskExamCustomerAnswerMapper.getByUid(taskId,userId);
        if (null==taskExamCustomerAnswer){
            taskExamCustomerAnswer = new TaskExamCustomerAnswer();
            taskExamCustomerAnswer.setCreateTime(now);
            taskExamCustomerAnswer.setCreateUser(userId);
            taskExamCustomerAnswer.setTaskExamId(taskId);
            List<TaskItemAnswerObject> taskItemAnswerObjects = Arrays.asList(new TaskItemAnswerObject(examPaperAnswer.getExamPaperId(), examPaperAnswer.getId(), examPaperAnswer.getStatus()));
            TextContent textContent = textContentService.jsonConvertInsert(taskItemAnswerObjects, now, null);
            textContentService.insertByFilter(textContent);
            taskExamCustomerAnswer.setTextContentId(textContent.getId());
            insertByFilter(taskExamCustomerAnswer);
        }else {
            TextContent textContent = textContentService.selectById(taskExamCustomerAnswer.getTextContentId());
            List<TaskItemAnswerObject> taskItemAnswerObjects = JsonUtil.toJsonListObject(textContent.getContent(),TaskItemAnswerObject.class);
            taskItemAnswerObjects.add(new TaskItemAnswerObject(examPaperAnswer.getExamPaperId(),examPaperAnswer.getId()));
            textContentService.jsonConvertUpdate(textContent,taskItemAnswerObjects,null);
            textContentService.updateByIdFilter(textContent);
        }

    }


}
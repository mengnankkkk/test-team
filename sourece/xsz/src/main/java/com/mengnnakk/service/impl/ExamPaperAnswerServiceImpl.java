package com.mengnnakk.service.impl;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.ExamPaperAnswerInfo;
import com.mengnnakk.entry.User;
import com.mengnnakk.mapper.ExamPaperAnswerMapper;
import com.mengnnakk.mapper.ExamPaperMapper;
import com.mengnnakk.mapper.QuestionMapper;
import com.mengnnakk.mapper.TaskExamCustomerAnswerMapper;
import com.mengnnakk.service.ExamPaperAnswerService;
import com.mengnnakk.service.ExamPaperQuestionCustomerAnswerService;
import com.mengnnakk.service.ExamPaperService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM;
import com.mengnnakk.viewmodel.student.exam.ExamPaperSubmitVM;
import com.mengnnakk.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamPaperAnswerServiceImpl extends ServiceImpl<ExamPaperAnswerMapper,ExamPaperAnswer> implements ExamPaperAnswerService {

    private final ExamPaperAnswerMapper examPaperAnswerMapper;
    private final ExamPaperMapper examPaperMapper;
    private final TextContentService textContentService;
    private final QuestionMapper questionMapper;
    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final TaskExamCustomerAnswerMapper taskExamCustomerAnswerMapper;

    @Autowired
    public ExamPaperAnswerServiceImpl(ExamPaperAnswerMapper examPaperAnswerMapper, ExamPaperMapper examPaperMapper, TextContentService textContentService, QuestionMapper questionMapper, ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService, TaskExamCustomerAnswerMapper taskExamCustomerAnswerMapper) {
        super();
        this.examPaperAnswerMapper = examPaperAnswerMapper;
        this.examPaperMapper = examPaperMapper;
        this.textContentService = textContentService;
        this.questionMapper = questionMapper;
        this.examPaperQuestionCustomerAnswerService = examPaperQuestionCustomerAnswerService;
        this.taskExamCustomerAnswerMapper = taskExamCustomerAnswerMapper;
    }


    @Override
    public PageInfo<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM) {
        return null;
    }

    @Override
    public ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user) {
        return null;
    }

    @Override
    public String judge(ExamPaperSubmitVM examPaperSubmitVM) {
        return null;
    }

    @Override
    public ExamPaperSubmitVM examPaperAnswerToVM(Integer id) {
        return null;
    }

    @Override
    public Integer selectAllCount() {
        return null;
    }

    @Override
    public List<Integer> selectMothCount() {
        return null;
    }

    @Override
    public PageInfo<ExamPaperAnswer> adminPage(ExamPaperAnswerPageRequestVM requestVM) {
        return null;
    }
}
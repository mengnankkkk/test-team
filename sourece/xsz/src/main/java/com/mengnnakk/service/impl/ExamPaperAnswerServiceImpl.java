package com.mengnnakk.service.impl;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.ExamPaperAnswerInfo;
import com.mengnnakk.entry.User;
import com.mengnnakk.mapper.ExamPaperAnswerMapper;
import com.mengnnakk.service.ExamPaperService;
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
        super(examPaperAnswerMapper);
        this.examPaperAnswerMapper = examPaperAnswerMapper;
        this.examPaperMapper = examPaperMapper;
        this.textContentService = textContentService;
        this.questionMapper = questionMapper;
        this.examPaperQuestionCustomerAnswerService = examPaperQuestionCustomerAnswerService;
        this.taskExamCustomerAnswerMapper = taskExamCustomerAnswerMapper;
    }



    /**
     * 学生考试记录分页
     *
     * @param requestVM 过滤条件
     * @return PageInfo<ExamPaperAnswer>
     */
    PageInfo<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    /**
     * 计算试卷提交结果(不入库)
     *
     * @param examPaperSubmitVM
     * @param user
     * @return
     */
    ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user);

    /**
     * 试卷批改
     * @param examPaperSubmitVM  examPaperSubmitVM
     * @return String
     */
    String judge(ExamPaperSubmitVM  examPaperSubmitVM);

    /**
     * 试卷答题信息转成ViewModel 传给前台
     *
     * @param id 试卷id
     * @return ExamPaperSubmitVM
     */
    ExamPaperSubmitVM examPaperAnswerToVM(Integer id);

    Integer selectAllCount();
    List<Integer> selectMothCount();
    PageInfo<ExamPaperAnswer> adminPage(ExamPaperAnswerPageRequestVM requestVM);


}
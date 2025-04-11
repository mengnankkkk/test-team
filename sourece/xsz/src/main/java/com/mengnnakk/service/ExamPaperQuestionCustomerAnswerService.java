package com.mengnnakk.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.ExamPaperQuestionCustomerAnswer;
import com.mengnnakk.entry.other.ExamPaperAnswerUpdate;
import com.mengnnakk.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.mengnnakk.viewmodel.student.question.answer.QuestionPageStudentRequestVM;

import java.util.List;

public interface ExamPaperQuestionCustomerAnswerService extends IService<ExamPaperQuestionCustomerAnswer> {
    PageInfo<ExamPaperQuestionCustomerAnswer> studentPage(QuestionPageStudentRequestVM requestVM);
    List<ExamPaperQuestionCustomerAnswer> selectListByPaperAnswerId(Integer id);

    /**
     * 试卷提交答案入库
     *
     * @param examPaperQuestionCustomerAnswers List<ExamPaperQuestionCustomerAnswer>
     */
    void  insertList(List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers);

    /**
     * 试卷问题答题信息转成ViewModel 传给前台
     *
     * @param qa ExamPaperQuestionCustomerAnswer
     * @return ExamPaperSubmitItemVM
     */
    ExamPaperSubmitItemVM examPaperQuestionCustomerAnswerToVM(ExamPaperQuestionCustomerAnswer qa);

    Integer selectAllCount();

    List<Integer> selectMothCount();

    int updateScore(List<ExamPaperAnswerUpdate> examPaperAnswerUpdates);

}
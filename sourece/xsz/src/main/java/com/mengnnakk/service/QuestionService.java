package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Question;
import com.mengnnakk.viewmodel.admin.question.QuestionEditRequestVM;
import com.mengnnakk.viewmodel.admin.question.QuestionPageRequestVM;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionService extends IService<Question> {
    PageInfo<Question> page(QuestionPageRequestVM requestVM);
    Question insertFullQuestion(QuestionEditRequestVM model,Integer userId);
    Question updateFullQuestion(QuestionEditRequestVM model);
    QuestionEditRequestVM getQuestionEditRequestVM(Integer questionId);
    QuestionEditRequestVM getQuestionEditRequestVM(Question question);
    Integer selectAllCount();

    List<Integer> selectMothCount();
}
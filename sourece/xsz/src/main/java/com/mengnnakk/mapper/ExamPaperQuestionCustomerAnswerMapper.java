package com.mengnnakk.mapper;


import com.mengnnakk.entry.ExamPaperQuestionCustomerAnswer;
import com.mengnnakk.entry.Question;
import com.mengnnakk.entry.other.ExamPaperAnswerUpdate;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.viewmodel.student.question.answer.QuestionPageStudentRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import javax.swing.plaf.PanelUI;
import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperQuestionCustomerAnswerMapper extends BaseMapper<ExamPaperQuestionCustomerAnswer>{
    List<ExamPaperQuestionCustomerAnswer> selectListByPaperAnswerId(Integer id);

    List<ExamPaperQuestionCustomerAnswer> studentPage(QuestionPageStudentRequestVM requestVM);

    int insertList(List<ExamPaperQuestionCustomerAnswer> list);

    Integer selectAllCount();
    List<KeyValue> selectCountByDate(@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    int updateScore(List<ExamPaperAnswerUpdate> examPaperAnswerUpdates);
}
package com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.ExamPaperQuestionCustomerAnswer;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.enums.QuestionTypeEnum;
import com.mengnnakk.entry.other.ExamPaperAnswerUpdate;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.mapper.ExamPaperQuestionCustomerAnswerMapper;
import com.mengnnakk.service.ExamPaperQuestionCustomerAnswerService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.ExamUtil;
import com.mengnnakk.utility.JsonUtil;
import com.mengnnakk.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.mengnnakk.viewmodel.student.question.answer.QuestionPageStudentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamPaperQuestionCustomerAnswerServiceImpl extends ServiceImpl<ExamPaperQuestionCustomerAnswerMapper, ExamPaperQuestionCustomerAnswer> implements ExamPaperQuestionCustomerAnswerService {

    private final ExamPaperQuestionCustomerAnswerMapper examPaperQuestionCustomerAnswerMapper;
    private final TextContentService textContentService;

    @Autowired
    public ExamPaperQuestionCustomerAnswerServiceImpl(ExamPaperQuestionCustomerAnswerMapper examPaperQuestionCustomerAnswerMapper, TextContentService textContentService) {
        this.examPaperQuestionCustomerAnswerMapper = examPaperQuestionCustomerAnswerMapper;
        this.textContentService = textContentService;
    }


    @Override
    public PageInfo<ExamPaperQuestionCustomerAnswer> studentPage(QuestionPageStudentRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(),requestVM.getPageSize(),"id desc").doSelectPageInfo(()->
                examPaperQuestionCustomerAnswerMapper.studentPage(requestVM)
                );
    }

    @Override
    public List<ExamPaperQuestionCustomerAnswer> selectListByPaperAnswerId(Integer id) {
        return examPaperQuestionCustomerAnswerMapper.selectListByPaperAnswerId(id);
    }

    @Override
    public void insertList(List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers) {
        examPaperQuestionCustomerAnswerMapper.insertList(examPaperQuestionCustomerAnswers);
    }

    @Override
    public ExamPaperSubmitItemVM examPaperQuestionCustomerAnswerToVM(ExamPaperQuestionCustomerAnswer qa) {
        ExamPaperSubmitItemVM examPaperSubmitItemVM = new ExamPaperSubmitItemVM();
        examPaperSubmitItemVM.setId(qa.getId());
        examPaperSubmitItemVM.setQuestionId(qa.getQuestionId());
        examPaperSubmitItemVM.setDoRight(qa.getDoRight());
        examPaperSubmitItemVM.setItemOrder(qa.getItemOrder());
        examPaperSubmitItemVM.setQuestionScore(ExamUtil.scoreToVM(qa.getQuestionScore()));
        examPaperSubmitItemVM.setScore(ExamUtil.scoreToVM(qa.getCustomerScore()));
        setSpecialToVM(examPaperSubmitItemVM, qa);
        return examPaperSubmitItemVM ;
    }

    private void setSpecialToVM(ExamPaperSubmitItemVM examPaperSubmitItemVM, ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(examPaperQuestionCustomerAnswer.getQuestionType());
        switch (questionTypeEnum){
            case MultipleChoice:
                examPaperSubmitItemVM.setContent(examPaperQuestionCustomerAnswer.getAnswer());
                examPaperSubmitItemVM.setContentArray(ExamUtil.contentToArray(examPaperQuestionCustomerAnswer.getAnswer()));
                break;
            case GapFilling:
                TextContent textContent = textContentService.selectById(examPaperQuestionCustomerAnswer.getTextContentId());
                List<String> correctAnswer = JsonUtil.toJsonListObject(textContent.getContent(),String.class);
                examPaperSubmitItemVM.setContentArray(correctAnswer);
                break;
            default:
                if (QuestionTypeEnum.needSaveTextContent(examPaperQuestionCustomerAnswer.getQuestionType())){
                    TextContent content = textContentService.selectById(examPaperQuestionCustomerAnswer.getTextContentId());
                    examPaperSubmitItemVM.setContent(content.getContent());

                }else {
                    examPaperSubmitItemVM.setContent(examPaperQuestionCustomerAnswer.getAnswer());
                }
        }
    }

    @Override
    public Integer selectAllCount() {
        return examPaperQuestionCustomerAnswerMapper.selectAllCount();
    }

    @Override
    public List<Integer> selectMothCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime  = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mountCount = examPaperQuestionCustomerAnswerMapper.selectCountByDate(startTime,endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md->{
            KeyValue keyValue = mountCount.stream().filter(kv->kv.getName().equals(md)).findAny().orElse(null);
            return null==keyValue?0:keyValue.getValue();
        }).collect(Collectors.toList());
    }

    @Override
    public int updateScore(List<ExamPaperAnswerUpdate> examPaperAnswerUpdates) {
        return examPaperQuestionCustomerAnswerMapper.updateScore(examPaperAnswerUpdates);
    }

}
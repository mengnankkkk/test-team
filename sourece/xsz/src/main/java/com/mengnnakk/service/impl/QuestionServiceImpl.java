package com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Question;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.enums.QuestionStatusEnum;
import com.mengnnakk.mapper.QuestionMapper;
import com.mengnnakk.service.QuestionService;
import com.mengnnakk.service.SubjectService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.ExamUtil;
import com.mengnnakk.utility.ModelMapperSingle;
import com.mengnnakk.viewmodel.admin.question.QuestionEditRequestVM;
import com.mengnnakk.viewmodel.admin.question.QuestionPageRequestVM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper,Question> implements QuestionService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final QuestionMapper questionMapper;
    private final TextContentService textContentService;
    private final SubjectService subjectService;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper, TextContentService textContentService, SubjectService subjectService) {
        this.textContentService = textContentService;
        this.questionMapper = questionMapper;
        this.subjectService = subjectService;
    }

    /**
     * 分页查询
     * @param requestVM
     * @return
     */
    @Override
    public PageInfo<Question> page(QuestionPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(),requestVM.getPageSize(),"id desc").doSelectPageInfo(()->
                questionMapper.page(requestVM)
        );
    }

    @Transactional
    @Override
    public Question insertFullQuestion(QuestionEditRequestVM model, Integer userId) {
        Date now  = new Date();
        Integer graderLevel = subjectService.levelBySubjectId(model.getSubjectId());

        //题干解析选项插入
        TextContent infoTextContent = new TextContent();
        infoTextContent.setCreateTime(now);
        setQuestionInfoFromVM(infoTextContent,model);//为止
        textContentService.insertByFilter(infoTextContent);

        Question question = new Question();
        question.setSubjectId(model.getSubjectId());
        question.setGradeLevel(graderLevel);
        question.setCreateTime(now);
        question.setQuestionType(model.getQuestionType());
        question.setStatus(QuestionStatusEnum.OK.getCode());
        question.setCorrectFromVM(model.getCorrect(), model.getCorrectArray());
        question.setScore(ExamUtil.scoreFromVM(model.getScore()));
        question.setDifficult(model.getDifficult());
        question.setInfoTextContentId(infoTextContent.getId());
        question.setCreateUser(userId);
        question.setDeleted(false);
        questionMapper.insert(question);
        return question;

    }

    @Override
    public Question updateFullQuestion(QuestionEditRequestVM model) {
        return null;
    }

    @Override
    public QuestionEditRequestVM getQuestionEditRequestVM(Integer questionId) {
        return null;
    }

    @Override
    public QuestionEditRequestVM getQuestionEditRequestVM(Question question) {
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
}



package com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.Question;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.enums.QuestionStatusEnum;
import com.mengnnakk.entry.enums.QuestionTypeEnum;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.entry.question.QuestionItemObject;
import com.mengnnakk.entry.question.QuestionObject;
import com.mengnnakk.mapper.QuestionMapper;
import com.mengnnakk.service.QuestionService;
import com.mengnnakk.service.SubjectService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.ExamUtil;
import com.mengnnakk.utility.JsonUtil;
import com.mengnnakk.utility.ModelMapperSingle;
import com.mengnnakk.viewmodel.admin.question.QuestionEditRequestVM;
import com.mengnnakk.viewmodel.admin.question.QuestionPageRequestVM;
import com.qiniu.util.Json;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.kerberos.KerberosTicket;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * item的设置
     * @param infoTextContent
     * @param model
     */
    public void setQuestionInfoFromVM(TextContent infoTextContent, QuestionEditRequestVM model) {
        List<QuestionItemObject> itemObjects = (List<QuestionItemObject>) model.getItems().stream().map(i->
        {
            QuestionItemObject item = new QuestionItemObject();
            item.setPrefix(i.getPrefix());
            item.setContent(i.getContent());
            item.setItemUuid(i.getItemUuid());
            item.setScore(ExamUtil.scoreFromVM(i.getScore()));
            return item;
        }).collect(Collectors.toList());
        QuestionObject questionObject = new QuestionObject();
        questionObject.setQuestionItemObjects(itemObjects);
        questionObject.setAnalyze(model.getAnalyze());
        questionObject.setTitleContent(model.getTitle());
        questionObject.setCorrect(model.getCorrect());
        infoTextContent.setContent(JsonUtil.toJsonStr(questionObject));
    }

    @Transactional
    @Override
    public Question updateFullQuestion(QuestionEditRequestVM model) {
        Integer gradeLevel  = subjectService.levelBySubjectId(model.getSubjectId());
        Question question = questionMapper.selectById(model.getId());
        question.setSubjectId(model.getSubjectId());
        question.setGradeLevel(gradeLevel);
        question.setScore(ExamUtil.scoreFromVM(model.getScore()));
        question.setDifficult(model.getDifficult());
        question.setCorrectFromVM(model.getCorrect(), model.getCorrectArray());
        questionMapper.updateById(question);

        //体感解析选项更新
        TextContent infoTextContent = textContentService.selectById(question.getInfoTextContentId());
        setQuestionInfoFromVM(infoTextContent, model);
        textContentService.updateByIdFilter(infoTextContent);

        return question;
    }

    @Override
    public QuestionEditRequestVM getQuestionEditRequestVM(Integer questionId) {
        //题目映射
        Question question = questionMapper.selectById(questionId);
        return getQuestionEditRequestVM(question);
    }

    @Override
    public QuestionEditRequestVM getQuestionEditRequestVM(Question question) {
        //题目映射
        TextContent questionInfoTextContent = textContentService.selectById(question.getInfoTextContentId());
        QuestionObject questionObject = JsonUtil.toJsonObject(questionInfoTextContent.getContent(),QuestionObject.class);
        QuestionEditRequestVM questionEditRequestVM = modelMapper.map(question,QuestionEditRequestVM.class);
        questionEditRequestVM.setTitle(questionObject.getTitleContent());

        //答案
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(question.getQuestionType());
        //题目类型
        switch (questionTypeEnum){
            case SingleChoice:
            case TrueFalse:
                questionEditRequestVM.setCorrect(question.getCorrect());
                break;
            case MultipleChoice:
                questionEditRequestVM.setCorrectArray(ExamUtil.contentToArray(question.getCorrect()));
                break;
            case GapFilling:
                List<String> correctContent = questionObject.getQuestionItemObjects().stream().map(d->d.getContent()).collect(Collectors.toList());
                questionEditRequestVM.setCorrectArray(correctContent);
                break;
            case ShortAnswer:
                questionEditRequestVM.setCorrect(questionObject.getCorrect());
                break;
            default:
                break;
        }
        questionEditRequestVM.setScore(ExamUtil.scoreToVM(question.getScore()));
        questionEditRequestVM.setAnalyze(questionObject.getAnalyze());


        //题目映射项
        List<QuestionEditRequestVM> editItems = questionObject.getQuestionItemObjects().stream().map(o->{
            QuestionEditRequestVM questionEditItemVM = modelMapper.map(o,QuestionEditRequestVM.class);
            if (o.getScore() != null) {
                questionEditItemVM.setScore(ExamUtil.scoreToVM(o.getScore()));
            }
            return questionEditItemVM;
        }).collect(Collectors.toList());
        questionEditRequestVM.setItems(editItems);
        return questionEditRequestVM;
    }

    @Override
    public Integer selectAllCount() {
        return questionMapper.selectAllCount();
    }

    /**
     * 查找月份
     * @return
     */
    @Override
    public List<Integer> selectMothCount() {
        Date startTime  = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        List<KeyValue> mouthCount = questionMapper.selectCountByDate(startTime,endTime);
        return mothStartToNowFormat.stream().map(md->{
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }
}



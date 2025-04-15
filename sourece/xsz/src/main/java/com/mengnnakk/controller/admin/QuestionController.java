package com.mengnnakk.controller.admin;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.base.SystemCode;
import com.mengnnakk.entry.Question;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.enums.QuestionTypeEnum;
import com.mengnnakk.entry.question.QuestionObject;
import com.mengnnakk.service.QuestionService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.*;
import com.mengnnakk.viewmodel.admin.question.QuestionEditRequestVM;
import com.mengnnakk.viewmodel.admin.question.QuestionPageRequestVM;
import com.mengnnakk.viewmodel.admin.question.QuestionResponseVM;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("AdminQuestionController")
@RequestMapping(value = "/api/admin/question")
public class QuestionController extends BaseApiController {
    private final QuestionService questionService;
    private final TextContentService textContentService;


    @Autowired
    public QuestionController(QuestionService questionService, TextContentService textContentService) {
        this.questionService = questionService;
        this.textContentService = textContentService;
    }

    /**
     * 页面展示
     * @param model
     * @return
     */
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public RestResponse<PageInfo<QuestionResponseVM>> pagelist(@RequestBody QuestionPageRequestVM model){
        PageInfo<Question> pageInfo = questionService.page(model);
        PageInfo<QuestionResponseVM> page = PageInfoHelper.copyMap(pageInfo, q->{
            QuestionResponseVM vm = modelMapper.map(q,QuestionResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(q.getCreateTime()));
            vm.setScore(ExamUtil.scoreToVM(q.getScore()));
            TextContent textContent = textContentService.selectById(q.getInfoTextContentId());
            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(),QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            return vm;
        });
        return RestResponse.ok(page);

    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid QuestionEditRequestVM model){
        RestResponse validQuestionEditRequestResult = validQuestionEditRequestVM(model);
        if(validQuestionEditRequestResult.getCode()!=SystemCode.OK.getCode()){
            return validQuestionEditRequestResult;
        }
        if (null==model.getId()){
            questionService.insertFullQuestion(model,getCurrentUser().getId());

        }else {
            questionService.updateFullQuestion(model);
        }
        return RestResponse.ok();
    }
    @RequestMapping(value = "/select/{id}",method = RequestMethod.POST)
    public RestResponse<QuestionEditRequestVM> select(@PathVariable Integer id){
        QuestionEditRequestVM  newVM = questionService.getQuestionEditRequestVM(id);
        return RestResponse.ok(newVM);
    }
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id){
        Question question = questionService.selectById(id);
        question.setDeleted(true);
        questionService.updateById(question);//应该是updateByIdFilter方法更好哈
        return RestResponse.ok();
    }


    /**
     * 题目的类型与分数之间关系
     * @param model
     * @return
     */
    private RestResponse validQuestionEditRequestVM(QuestionEditRequestVM model) {
        int qType  = model.getQuestionType().intValue();
        boolean requireCorrect = qType == QuestionTypeEnum.SingleChoice.getCode()||qType==QuestionTypeEnum.TrueFalse.getCode();
        if (requireCorrect){
            if (StringUtils.isBlank(model.getCorrect())){
                String erroMsg = ErrorUtil.parameterErrorFormat("correct","不能为空");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(),erroMsg);
            }
        }
        if (qType==QuestionTypeEnum.GapFilling.getCode()){
            Integer fileSumScore = model.getItems().stream().mapToInt(d->ExamUtil.scoreFromVM(d.getScore())).sum();
            Integer questionSocre = ExamUtil.scoreFromVM(model.getScore());
            if (!fileSumScore.equals(questionSocre)){
                String errorMsg = ErrorUtil.parameterErrorFormat("score","空分数和题目总分不相等");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);

            }
        }
        return RestResponse.ok();
    }
}

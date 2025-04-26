package com.mengnnakk.controller.student;


import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.TaskExam;
import com.mengnnakk.entry.TaskExamCustomerAnswer;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.enums.ExamPaperTypeEnum;
import com.mengnnakk.entry.task.TaskItemAnswerObject;
import com.mengnnakk.entry.task.TaskItemObject;
import com.mengnnakk.service.*;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.JsonUtil;
import com.mengnnakk.viewmodel.admin.dashboard.IndexVM;
import com.mengnnakk.viewmodel.student.dashboard.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController("StudentDashboardController")
@RequestMapping(value = "/api/student/dashboard")
public class DashboardController extends BaseApiController {
    private final UserService userService;
    private final ExamPaperService examPaperService;
    private final QuestionService questionService;
    private final TaskExamService taskExamService;
    private final TaskExamCustomerAnswerService taskExamCustomerAnswerService;
    private final TextContentService textContentService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public DashboardController(UserService userService, ExamPaperService examPaperService, QuestionService questionService, TaskExamService taskExamService, TaskExamCustomerAnswerService taskExamCustomerAnswerService, TextContentService textContentService) {
        this.userService = userService;
        this.examPaperService = examPaperService;
        this.questionService = questionService;
        this.taskExamService = taskExamService;
        this.taskExamCustomerAnswerService = taskExamCustomerAnswerService;
        this.textContentService = textContentService;
    }
    @RequestMapping(value ="/index",method = RequestMethod.POST)
    public RestResponse<IndexVM> index(){
        IndexVM indexVM = new IndexVM();
        User user = getCurrentUser();

        PaperFilter fixedPaperFilter = new PaperFilter();
        fixedPaperFilter.setGradeLevel(user.getUserLevel());
        fixedPaperFilter.setExamPaperType(ExamPaperTypeEnum.Fixed.getCode());
        indexVM.setFixedPaper(examPaperService.indexPaper(fixedPaperFilter));

        PaperFilter timeLimitPaperFilter = new PaperFilter();
        timeLimitPaperFilter.setDateTime(new Date());
        timeLimitPaperFilter.setGradeLevel(user.getUserLevel());
        timeLimitPaperFilter.setExamPaperType(ExamPaperTypeEnum.TimeLimit.getCode());


        List<PaperInfo> limitPaper = examPaperService.indexPaper(timeLimitPaperFilter);
        List<PaperInfoVM> paperInfoVMS = limitPaper.stream().map(d -> {
            PaperInfoVM vm = modelMapper.map(d, PaperInfoVM.class);
            vm.setStartTime(DateTimeUtil.dateFormat(d.getLimitStartTime()));
            vm.setEndTime(DateTimeUtil.dateFormat(d.getLimitEndTime()));
            return vm;
        }).collect(Collectors.toList());
        indexVM.setTimeLimitPaper(paperInfoVMS);
        return RestResponse.ok(indexVM);

    }
    private List<TaskItemPaperVm> getTaskItemPaperVm(Integer tFrameId, TaskExamCustomerAnswer taskExamCustomerAnswer){
        TextContent textContent = textContentService.selectById(tFrameId);
        List<TaskItemObject> paperItems = JsonUtil.toJsonListObject(textContent.getContent(),TaskItemObject.class);//反序列化

        List<TaskItemAnswerObject > answerPaperItems = null;
        if (null!=taskExamCustomerAnswer){
            TextContent answerTextContent = textContentService.selectById(taskExamCustomerAnswer.getTextContentId());
            answerPaperItems = JsonUtil.toJsonListObject(answerTextContent.getContent(),TaskItemAnswerObject.class);
        }
        List<TaskItemAnswerObject> finalAnswerPaperItems = answerPaperItems;
        return paperItems.stream().map(p->{
            TaskItemPaperVm ivm = new TaskItemPaperVm();
            ivm.setExamPaperId(p.getExamPaperId());
            ivm.setExamPaperName(p.getExamPaperName());
            if (null!=finalAnswerPaperItems){
                finalAnswerPaperItems.stream().filter(a -> a.getExamPaperId().equals(p.getExamPaperId())).findFirst()
                        .ifPresent(a->{
                            ivm.setExamPaperAnswerId(a.getExamPaperAnswerId());
                            ivm.setStatus(a.getStatus());
                        });
            }
            return ivm;
        }).collect(Collectors.toList());

    }
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RestResponse<List<TaskItemVm>> task() {
        User user = getCurrentUser();
        List<TaskExam> taskExams = taskExamService.getByGradeLevel(user.getUserLevel());
        if (taskExams.size() == 0) {
            return RestResponse.ok(new ArrayList<>());
        }
        List<Integer> tIds = taskExams.stream().map(taskExam -> taskExam.getId()).collect(Collectors.toList());
        List<TaskExamCustomerAnswer> taskExamCustomerAnswers = taskExamCustomerAnswerService.selectByTUid(tIds, user.getId());
        List<TaskItemVm> vm = taskExams.stream().map(t -> {
            TaskItemVm itemVm = new TaskItemVm();
            itemVm.setId(t.getId());
            itemVm.setTitle(t.getTitle());
            TaskExamCustomerAnswer taskExamCustomerAnswer = taskExamCustomerAnswers.stream()
                    .filter(tc -> tc.getTaskExamId().equals(t.getId())).findFirst().orElse(null);
            List<TaskItemPaperVm> paperItemVMS = getTaskItemPaperVm(t.getFrameTextContentId(), taskExamCustomerAnswer);
            itemVm.setPaperItems(paperItemVMS);
            return itemVm;
        }).collect(Collectors.toList());
        return RestResponse.ok(vm);
    }
}

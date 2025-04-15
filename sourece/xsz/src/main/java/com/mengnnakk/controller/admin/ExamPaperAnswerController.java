package com.mengnnakk.controller.admin;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.ExamPaperAnswer;
import com.mengnnakk.entry.Subject;
import com.mengnnakk.entry.User;
import com.mengnnakk.service.ExamPaperAnswerService;
import com.mengnnakk.service.SubjectService;
import com.mengnnakk.service.UserService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.ExamUtil;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.paper.ExamAnswerResponseVM;
import com.mengnnakk.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM;
import com.mengnnakk.viewmodel.student.exampaper.ExamPaperAnswerPageResponseVM;
import com.mengnnakk.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("AdminExamPaperAnswerController")
@RequestMapping("/api/admin/examPaperAnswer")
public class ExamPaperAnswerController extends BaseApiController {
    private final ExamPaperAnswerService examPaperAnswerService;
    private final SubjectService subjectService;
    private final UserService userService;

    @Autowired
    public ExamPaperAnswerController(ExamPaperAnswerService examPaperAnswerService, SubjectService subjectService, UserService userService) {
        this.examPaperAnswerService = examPaperAnswerService;
        this.subjectService = subjectService;
        this.userService = userService;
    }
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamPaperAnswerPageResponseVM>> pageJuageList(@RequestBody ExamPaperAnswerPageRequestVM model){
        PageInfo<ExamPaperAnswer> pageInfo = examPaperAnswerService.adminPage(model);
        PageInfo<ExamPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo,e->{
            ExamPaperAnswerPageResponseVM vm = modelMapper.map(e, ExamPaperAnswerPageResponseVM.class);
            Subject subject = subjectService.selectById(vm.getSubjectId());
            vm.setDoTime(ExamUtil.secondToVM(e.getDoTime()));
            vm.setSystemScore(ExamUtil.scoreToVM(e.getSystemScore()));
            vm.setUserScore(ExamUtil.scoreToVM(e.getUserScore()));
            vm.setPaperScore(ExamUtil.scoreToVM(e.getPaperScore()));
            vm.setSubjectName(subject.getName());
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            User user = userService.selectById(e.getCreateUser());
            vm.setUserName(user.getUserName());
            return vm;

        });
        return RestResponse.ok(page);

    }
}

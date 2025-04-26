package com.mengnnakk.controller.student;


import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.Subject;
import com.mengnnakk.entry.User;
import com.mengnnakk.service.SubjectService;
import com.mengnnakk.viewmodel.student.education.SubjectEditRequestVM;
import com.mengnnakk.viewmodel.student.education.SubjectVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockAsyncContext;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("StudentEducationController")
@RequestMapping(value = "/api/student/education")
public class EducationController extends BaseApiController {
    private final SubjectService subjectService;

    @Autowired
    public EducationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/subject/list")
    public RestResponse<List<SubjectVM>> list(){
        User user = getCurrentUser();
        List<Subject> subjects = subjectService.getSubjectByLevel(user.getUserLevel());
        List<SubjectVM> subjectVMS = subjects.stream().map(d->{
            SubjectVM subjectVM = modelMapper.map(d,SubjectVM.class);
            subjectVM.setId(String.valueOf(d.getId()));
            return subjectVM;
        }).collect(Collectors.toList());
        return RestResponse.ok(subjectVMS);
    }
    @PostMapping("/subject/select/{id}")
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id){
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject,SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

}

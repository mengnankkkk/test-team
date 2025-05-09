package com.mengnnakk.controller.admin;


import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.Subject;
import com.mengnnakk.service.SubjectService;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.education.SubjectEditRequestVM;
import com.mengnnakk.viewmodel.admin.education.SubjectPageRequestVM;
import com.mengnnakk.viewmodel.admin.education.SubjectResponseVM;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("AdminEducationController")
@RequestMapping(value = "/api/admin/education")
public class EducationController extends BaseApiController {
    private final SubjectService subjectService;

    @Autowired
    public EducationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    @RequestMapping(value = "/subject/list",method = RequestMethod.POST)
    public RestResponse<List<Subject>> list(){
        List<Subject> subjects = subjectService.allSubject();
        return RestResponse.ok(subjects);
    }
    @RequestMapping(value = "/subject/page",method = RequestMethod.POST)
    public RestResponse<PageInfo<SubjectResponseVM>> pagelist(@RequestBody SubjectPageRequestVM model){
        PageInfo<Subject> pageInfo = subjectService.page(model);
        PageInfo<SubjectResponseVM> page = PageInfoHelper.copyMap(pageInfo,e->modelMapper.map(e,SubjectResponseVM.class));
        return RestResponse.ok(page);
    }
    @RequestMapping(value = "/subject/edit",method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid SubjectEditRequestVM model){
        Subject subject = modelMapper.map(model,Subject.class);
        if (model.getId()==null){
            subject.setDeleted(false);
            subjectService.insert(subject);//此处方法错误
        }else {
            subjectService.updateByIdFilter(subject);
        }
        return RestResponse.ok();
    }
    @RequestMapping(value = "/subject/select/{id}",method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id){
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm  = modelMapper.map(subject,SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }
    @RequestMapping(value = "/subject/delete/{id}",method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id){
        Subject subject = subjectService.selectById(id);
        subject.setDeleted(true);
        subjectService.updateByIdFilter(subject);
        return RestResponse.ok();
    }


}

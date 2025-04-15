package com.mengnnakk.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.mapper.ExamPaperMapper;
import com.mengnnakk.service.ExamPaperService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mengnnakk.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mengnnakk.viewmodel.admin.exam.ExamResponseVM;
import com.sun.org.apache.bcel.internal.generic.BALOAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.validation.Valid;
import javax.xml.crypto.Data;

@RestController("AdminExamPaperController")
@RequestMapping(value = "/api/admin/exam/paper")
public class ExamPaperController extends BaseApiController {
    private final ExamPaperService examPaperService;

    @Autowired
    public ExamPaperController(ExamPaperService examPaperService) {
        this.examPaperService = examPaperService;
    }
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamResponseVM>> pagelist(@RequestBody ExamPaperPageRequestVM model){
        PageInfo<ExamPaper> pageInfo = examPaperService.page(model);
        PageInfo<ExamResponseVM> page = PageInfoHelper.copyMap(pageInfo,e->{
            ExamResponseVM vm = modelMapper.map(e,ExamResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> edit (@RequestBody @Valid ExamPaperEditRequestVM model){
        ExamPaper examPaper = examPaperService.savePaperFromVM(model,getCurrentUser());
        ExamPaperEditRequestVM newVM = examPaperService.examPaperToVM(examPaper.getId());
        return RestResponse.ok(newVM);
    }
    @RequestMapping(value = "/select/{id}",method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> select(@PathVariable Integer id){
        ExamPaperEditRequestVM vm = examPaperService.examPaperToVM(id);
        return RestResponse.ok(vm);
    }
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id){
        ExamPaper examPaper = examPaperService.selectById(id);
        examPaper.setDeleted(true);
        examPaperService.updateById(examPaper);//方法名错了
        return RestResponse.ok();
    }
}

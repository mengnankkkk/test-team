package com.mengnnakk.controller.admin;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.base.RestResponse;
import com.mengnnakk.entry.TaskExam;
import com.mengnnakk.service.TaskExamService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.PageInfoHelper;
import com.mengnnakk.viewmodel.admin.task.TaskPageRequestVM;
import com.mengnnakk.viewmodel.admin.task.TaskPageResponseVM;
import com.mengnnakk.viewmodel.admin.task.TaskRequestVM;
import io.swagger.models.auth.In;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import javax.validation.Valid;

@RestController("AdminTaskController")
@RequestMapping(value = "/api/admin/task")
public class TaskController extends BaseApiController {
    private final TaskExamService taskExamService;


    @Autowired
    public TaskController(TaskExamService taskExamService) {
        this.taskExamService = taskExamService;
    }
    @RequestMapping(value = "/page")
    public RestResponse<PageInfo<TaskPageResponseVM>> pagelist(@RequestBody TaskPageRequestVM model){
        PageInfo<TaskExam> pageInfo = taskExamService.page(model);
        PageInfo<TaskPageResponseVM> page = PageInfoHelper.copyMap(pageInfo,m->{
            TaskPageResponseVM vm = modelMapper.map(m,TaskPageResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(m.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid TaskRequestVM model){
        taskExamService.edit(model,getCurrentUser());
        TaskRequestVM vm = taskExamService.taskExamToVM(model.getId());
        return RestResponse.ok(vm);
    }
    @RequestMapping(value = "/select/{id}",method = RequestMethod.POST)
    public RestResponse<TaskRequestVM> select(@PathVariable Integer id){
        TaskRequestVM vm = taskExamService.taskExamToVM(id);
        return RestResponse.ok(vm);
    }
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id){
        TaskExam taskExam = taskExamService.selectById(id);
        taskExam.setDeleted(true);
        taskExamService.updateById(taskExam);//方法名不对
        return RestResponse.ok();
    }

}

package com.mengnnakk.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.TaskExam;
import com.mengnnakk.entry.User;
import com.mengnnakk.viewmodel.admin.task.TaskPageRequestVM;
import com.mengnnakk.viewmodel.admin.task.TaskRequestVM;

import java.util.List;

public interface TaskExamService extends IService<TaskExam> {
    PageInfo<TaskExam> page(TaskPageRequestVM requestVM);
    void edit(TaskPageRequestVM model, User user);

    TaskRequestVM taskExamToVM(Integer id);

    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}
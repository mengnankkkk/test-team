package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.TaskExam;
import com.mengnnakk.entry.User;
import com.mengnnakk.viewmodel.admin.task.TaskPageRequestVM;
import com.mengnnakk.viewmodel.admin.task.TaskRequestVM;

import java.util.List;

public interface TaskExamService extends IService<TaskExam> {
    // 分页查询
    PageInfo<TaskExam> page(TaskPageRequestVM requestVM);

    // 添加或编辑任务
    void edit(TaskRequestVM model, User user);

    // 根据 ID 获取任务详情（用于编辑页面回显）
    TaskRequestVM taskExamToVM(Integer id);

    // 根据年级获取任务列表
    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}

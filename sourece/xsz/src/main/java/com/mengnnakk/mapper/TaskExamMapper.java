package com.mengnnakk.mapper;

import com.mengnnakk.entry.TaskExam;
import com.mengnnakk.viewmodel.admin.task.TaskPageRequestVM;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskExamMapper extends BaseMapper<TaskExam>{

    List<TaskExam> page(TaskPageRequestVM requestVM);
    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}
package com.mengnnakk.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.entry.TaskExam;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.task.TaskItemObject;
import com.mengnnakk.mapper.ExamPaperMapper;
import com.mengnnakk.mapper.TaskExamMapper;
import com.mengnnakk.service.TaskExamService;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.utility.JsonUtil;
import com.mengnnakk.utility.ModelMapperSingle;
import com.mengnnakk.viewmodel.admin.exam.ExamResponseVM;
import com.mengnnakk.viewmodel.admin.task.TaskPageRequestVM;
import com.mengnnakk.viewmodel.admin.task.TaskRequestVM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskExamServiceImpl extends ServiceImpl<TaskExamMapper, TaskExam> implements TaskExamService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();

    private final TaskExamMapper taskExamMapper;
    private final TextContentService textContentService;
    private final ExamPaperMapper examPaperMapper;

    @Autowired
    public TaskExamServiceImpl(TaskExamMapper taskExamMapper, TextContentService textContentService, ExamPaperMapper examPaperMapper) {
        this.taskExamMapper = taskExamMapper;
        this.textContentService = textContentService;
        this.examPaperMapper = examPaperMapper;
    }

    @Override
    public PageInfo<TaskExam> page(TaskPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> taskExamMapper.page(requestVM));
    }

    @Transactional
    @Override
    public void edit(TaskRequestVM model, User user) {
        ActionEnum actionEnum = (model.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        TaskExam taskExam;

        if (actionEnum == ActionEnum.ADD) {
            Date now = new Date();
            taskExam = modelMapper.map(model, TaskExam.class);
            taskExam.setCreateUser(user.getId());
            taskExam.setCreateUserName(user.getUserName());
            taskExam.setCreateTime(now);
            taskExam.setDeleted(false);

            // 保存任务结构
            TextContent textContent = textContentService.jsonConvertInsert(model.getPaperItems(), now, p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.insertByFilter(textContent);
            taskExam.setFrameTextContentId(textContent.getId());
            taskExamMapper.insertSelective(taskExam);
        } else {
            taskExam = taskExamMapper.selectByPrimaryKey(model.getId());
            modelMapper.map(model, taskExam);

            TextContent textContent = textContentService.selectById(taskExam.getFrameTextContentId());

            // 清空试卷任务ID
            List<Integer> existingPaperIds = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class)
                    .stream().map(TaskItemObject::getExamPaperId).collect(Collectors.toList());
            examPaperMapper.clearTaskPaper(existingPaperIds);

            // 更新任务结构
            textContentService.jsonConvertUpdate(textContent, model.getPaperItems(), p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.updateByIdFilter(textContent);
            taskExamMapper.updateByPrimaryKey(taskExam);

            // 更新试卷的 taskId
            List<Integer> updatedPaperIds = model.getPaperItems().stream()
                    .map(d -> d.getId()).collect(Collectors.toList());
            examPaperMapper.updateTaskPaper(taskExam.getId(), updatedPaperIds);
            model.setId(taskExam.getId());
        }
    }

    @Override
    public TaskRequestVM taskExamToVM(Integer id) {
        TaskExam taskExam = taskExamMapper.selectByPrimaryKey(id);
        TaskRequestVM vm = modelMapper.map(taskExam, TaskRequestVM.class);

        TextContent textContent = textContentService.selectById(taskExam.getFrameTextContentId());
        List<ExamResponseVM> examResponseVMS = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class)
                .stream().map(tk -> {
                    ExamPaper examPaper = examPaperMapper.selectById(tk.getExamPaperId());
                    ExamResponseVM examResponseVM = modelMapper.map(examPaper, ExamResponseVM.class);
                    examResponseVM.setCreateTime(DateTimeUtil.dateFormat(examResponseVM.getCreateTime()));
                    return examResponseVM;
                }).collect(Collectors.toList());

        vm.setPaperItems(examResponseVMS);
        return vm;
    }

    @Override
    public List<TaskExam> getByGradeLevel(Integer gradeLevel) {
        return taskExamMapper.getByGradeLevel(gradeLevel);
    }

    public enum ActionEnum {
        ADD,
        UPDATE
    }
}

package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.ExamPaper;
import com.mengnnakk.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mengnnakk.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mengnnakk.viewmodel.student.dashboard.PaperFilter;
import com.mengnnakk.viewmodel.student.exam.ExamPaperPageVM;

import java.util.List;

public interface ExamPaperService extends IService<ExamPaper> {

    PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM  requestVM);
    PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM);
    ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM);
    ExamPaperEditRequestVM examPaperToVM(Integer id);
    List<PageInfo> indexPaper(PaperFilter paperFilter);
    Integer selectAllCount();
    List<Integer> selectMothCount();
}